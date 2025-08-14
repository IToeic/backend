package kr.mojuk.itoeic.user;

import kr.mojuk.itoeic.email.EmailService;
import kr.mojuk.itoeic.user.dto.LoginRequestDto;
import kr.mojuk.itoeic.user.dto.LoginResponseDto;
import kr.mojuk.itoeic.user.dto.SignupRequestDto;
import kr.mojuk.itoeic.user.dto.SignupResponseDto;
import kr.mojuk.itoeic.user.dto.MyPageResponseDto;
import kr.mojuk.itoeic.user.dto.UpdateProfileRequestDto;
import kr.mojuk.itoeic.user.dto.UpdateProfileResponseDto;
import kr.mojuk.itoeic.user.dto.MyPageAccessRequestDto;
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
    
    // 마이페이지 조회
    public MyPageResponseDto getMyPageInfo(String userId) {
        return usersRepository.findByUserId(userId)
                .filter(user -> !user.getIsDeleted())
                .map(user -> MyPageResponseDto.builder()
                        .success(true)
                        .message("마이페이지 정보 조회 성공")
                        .userId(user.getUserId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build())
                .orElse(MyPageResponseDto.builder()
                        .success(false)
                        .message("사용자를 찾을 수 없습니다.")
                        .build());
    }
    
    // 프로필 수정
    public UpdateProfileResponseDto updateProfile(String userId, UpdateProfileRequestDto requestDto) {
        try {
            System.out.println("UserService updateProfile - userId: " + userId);
            System.out.println("UserService updateProfile - newName: " + requestDto.getNewName());
            System.out.println("UserService updateProfile - newPassword: " + (requestDto.getNewPassword() != null ? "***" : "null"));
            
            return usersRepository.findByUserId(userId)
                    .filter(user -> !user.getIsDeleted())
                    .map(user -> {
                        System.out.println("UserService updateProfile - found user: " + user.getName());
                        boolean isModified = false;
                        
                        // 이름 수정 (값이 있고 기존과 다른 경우에만)
                        if (requestDto.getNewName() != null && !requestDto.getNewName().trim().isEmpty() 
                                && !requestDto.getNewName().trim().equals(user.getName())) {
                            System.out.println("UserService updateProfile - updating name from '" + user.getName() + "' to '" + requestDto.getNewName().trim() + "'");
                            user.setName(requestDto.getNewName().trim());
                            isModified = true;
                        }
                        
                        // 비밀번호 수정 (값이 있는 경우에만)
                        if (requestDto.getNewPassword() != null && !requestDto.getNewPassword().trim().isEmpty()) {
                            System.out.println("UserService updateProfile - updating password");
                            String encodedNewPassword = passwordEncoder.encode(requestDto.getNewPassword().trim());
                            user.setPasswordHash(encodedNewPassword);
                            isModified = true;
                        }
                        
                        // 변경사항이 있는 경우에만 저장
                        if (isModified) {
                            System.out.println("UserService updateProfile - saving changes");
                            usersRepository.save(user);
                            return UpdateProfileResponseDto.builder()
                                    .success(true)
                                    .message("프로필이 성공적으로 수정되었습니다.")
                                    .build();
                        } else {
                            System.out.println("UserService updateProfile - no changes to save");
                            return UpdateProfileResponseDto.builder()
                                    .success(true)
                                    .message("수정할 내용이 없습니다.")
                                    .build();
                        }
                    })
                    .orElse(UpdateProfileResponseDto.builder()
                            .success(false)
                            .message("사용자를 찾을 수 없습니다.")
                            .build());
        } catch (Exception e) {
            System.err.println("UserService updateProfile error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // 마이페이지 접근 확인 (비밀번호 검증)
    public MyPageResponseDto verifyMyPageAccess(String userId, MyPageAccessRequestDto requestDto) {
        return usersRepository.findByUserId(userId)
                .filter(user -> !user.getIsDeleted())
                .filter(user -> passwordEncoder.matches(requestDto.getPassword(), user.getPasswordHash()))
                .map(user -> MyPageResponseDto.builder()
                        .success(true)
                        .message("마이페이지 접근이 허용되었습니다.")
                        .userId(user.getUserId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build())
                .orElse(MyPageResponseDto.builder()
                        .success(false)
                        .message("비밀번호가 올바르지 않거나 사용자를 찾을 수 없습니다.")
                        .build());
    }
} 