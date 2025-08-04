package kr.mojuk.itoeic.user;

import kr.mojuk.itoeic.email.EmailService;
import kr.mojuk.itoeic.user.dto.LoginRequestDto;
import kr.mojuk.itoeic.user.dto.LoginResponseDto;
import kr.mojuk.itoeic.user.dto.SignupRequestDto;
import kr.mojuk.itoeic.user.dto.SignupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        // 사용자 ID로 사용자 정보 조회
        return usersRepository.findByUserId(loginRequestDto.getUserId())
                .filter(user -> !user.getIsDeleted()) // 삭제되지 않은 사용자만
                .filter(user -> passwordEncoder.matches(loginRequestDto.getPassword(), user.getPasswordHash())) // 비밀번호 확인
                .map(user -> LoginResponseDto.builder()
                        .success(true)
                        .message("로그인 성공")
                        .userId(user.getUserId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build())
                .orElse(LoginResponseDto.builder()
                        .success(false)
                        .message("아이디 또는 비밀번호가 올바르지 않습니다.")
                        .build());
    }
    
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        // 사용자 ID 중복 체크
        if (usersRepository.existsByUserId(signupRequestDto.getUserId())) {
            return SignupResponseDto.builder()
                    .success(false)
                    .message("이미 사용 중인 아이디입니다.")
                    .build();
        }
        
        // 이메일 중복 체크
        if (usersRepository.existsByEmail(signupRequestDto.getEmail())) {
            return SignupResponseDto.builder()
                    .success(false)
                    .message("이미 사용 중인 이메일입니다.")
                    .build();
        }
        
        // 이메일 인증 확인
        if (!emailService.isEmailVerified(signupRequestDto.getEmail())) {
            return SignupResponseDto.builder()
                    .success(false)
                    .message("이메일 인증이 완료되지 않았습니다.")
                    .build();
        }
        
        // 성과 이름을 합쳐서 전체 이름 생성
        String fullName = signupRequestDto.getFirstName() + signupRequestDto.getLastName();
        
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        
        // 사용자 엔티티 생성
        Users newUser = Users.builder()
                .userId(signupRequestDto.getUserId())
                .name(fullName)
                .passwordHash(encodedPassword)
                .email(signupRequestDto.getEmail())
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .emailVerified(true)
                .build();
        
        // 데이터베이스에 저장
        usersRepository.save(newUser);
        
        // 이메일 인증 정보 삭제
        emailService.deleteVerification(signupRequestDto.getEmail());
        
        return SignupResponseDto.builder()
                .success(true)
                .message("회원가입이 완료되었습니다.")
                .build();
    }
} 