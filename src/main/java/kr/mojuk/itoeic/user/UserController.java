package kr.mojuk.itoeic.user;

import kr.mojuk.itoeic.email.EmailService;
import kr.mojuk.itoeic.email.dto.EmailVerificationRequestDto;
import kr.mojuk.itoeic.email.dto.EmailVerificationResponseDto;
import kr.mojuk.itoeic.email.dto.EmailCodeVerificationRequestDto;
import kr.mojuk.itoeic.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import kr.mojuk.itoeic.user.dto.MyPageAccessRequestDto;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final EmailService emailService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpSession session) {
        LoginResponseDto response = userService.login(loginRequestDto);
        
        if (response.isSuccess()) {
            // 로그인 성공 시 세션에 사용자 ID 저장
            session.setAttribute("userId", response.getUserId());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃되었습니다.");
    }
    
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
        try {
            SignupResponseDto response = userService.signup(signupRequestDto);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(SignupResponseDto.builder()
                    .success(false)
                    .message("회원가입 처리 중 오류가 발생했습니다.")
                    .build());
        }
    }
    
    @PostMapping("/send-verification-email")
    public ResponseEntity<EmailVerificationResponseDto> sendVerificationEmail(@RequestBody EmailVerificationRequestDto request) {
        try {
            emailService.sendVerificationEmail(request.getEmail());
            return ResponseEntity.ok(EmailVerificationResponseDto.builder()
                    .success(true)
                    .message("인증 이메일이 발송되었습니다.")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(EmailVerificationResponseDto.builder()
                    .success(false)
                    .message("이메일 발송에 실패했습니다.")
                    .build());
        }
    }
    
    @PostMapping("/verify-email-code")
    public ResponseEntity<EmailVerificationResponseDto> verifyEmailCode(@RequestBody EmailCodeVerificationRequestDto request) {
        boolean isVerified = emailService.verifyCode(request.getEmail(), request.getCode());
        
        if (isVerified) {
            return ResponseEntity.ok(EmailVerificationResponseDto.builder()
                    .success(true)
                    .message("이메일 인증이 완료되었습니다.")
                    .build());
        } else {
            return ResponseEntity.badRequest().body(EmailVerificationResponseDto.builder()
                    .success(false)
                    .message("인증 코드가 올바르지 않거나 만료되었습니다.")
                    .build());
        }
    }
    
    // 마이페이지 접근 확인 (비밀번호 검증)
    @PostMapping("/mypage/verify")
    public ResponseEntity<MyPageResponseDto> verifyMyPageAccess(
            HttpSession session,
            @RequestBody MyPageAccessRequestDto requestDto) {
        try {
            String userId = (String) session.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(401).body(MyPageResponseDto.builder()
                        .success(false)
                        .message("로그인이 필요합니다.")
                        .build());
            }
            
            MyPageResponseDto response = userService.verifyMyPageAccess(userId, requestDto);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(MyPageResponseDto.builder()
                    .success(false)
                    .message("마이페이지 접근 확인 중 오류가 발생했습니다.")
                    .build());
        }
    }
    
    // 마이페이지 조회
    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponseDto> getMyPageInfo(HttpSession session) {
        try {
            String userId = (String) session.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(401).body(MyPageResponseDto.builder()
                        .success(false)
                        .message("로그인이 필요합니다.")
                        .build());
            }
            
            MyPageResponseDto response = userService.getMyPageInfo(userId);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            System.err.println("Error in getMyPageInfo: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(MyPageResponseDto.builder()
                    .success(false)
                    .message("마이페이지 정보 조회 중 오류가 발생했습니다.")
                    .build());
        }
    }
    
    // 프로필 수정
    @PutMapping("/mypage")
    public ResponseEntity<UpdateProfileResponseDto> updateProfile(
            HttpSession session,
            @RequestBody UpdateProfileRequestDto requestDto) {
        try {
            String userId = (String) session.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(401).body(UpdateProfileResponseDto.builder()
                        .success(false)
                        .message("로그인이 필요합니다.")
                        .build());
            }
            UpdateProfileResponseDto response = userService.updateProfile(userId, requestDto);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(UpdateProfileResponseDto.builder()
                    .success(false)
                    .message("프로필 수정 중 오류가 발생했습니다.")
                    .build());
        }
    }
} 