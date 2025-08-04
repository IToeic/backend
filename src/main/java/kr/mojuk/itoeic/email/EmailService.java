package kr.mojuk.itoeic.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final EmailVerificationRepository emailVerificationRepository;
    
    public void sendVerificationEmail(String email) {
        // 6자리 인증 코드 생성
        String verificationCode = generateVerificationCode();
        
        // 기존 인증 정보가 있으면 삭제
        emailVerificationRepository.deleteByEmail(email);
        
        // 새로운 인증 정보 저장 (5분 유효)
        EmailVerifications verification = EmailVerifications.builder()
                .email(email)
                .code(verificationCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .isVerified(false)
                .build();
        
        emailVerificationRepository.save(verification);
        
        // 이메일 발송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[IToeic] 이메일 인증 코드");
        message.setText("안녕하세요!\n\n" +
                "IToeic 회원가입을 위한 이메일 인증 코드입니다.\n\n" +
                "인증 코드: " + verificationCode + "\n\n" +
                "이 코드는 5분간 유효합니다.\n" +
                "본인이 요청하지 않은 경우 이 이메일을 무시하세요.\n\n" +
                "감사합니다.");
        
        mailSender.send(message);
    }
    
    public boolean verifyCode(String email, String code) {
        return emailVerificationRepository.findByEmail(email)
                .filter(verification -> !verification.getIsVerified())
                .filter(verification -> verification.getExpiresAt().isAfter(LocalDateTime.now()))
                .filter(verification -> verification.getCode().equals(code))
                .map(verification -> {
                    verification.setIsVerified(true);
                    emailVerificationRepository.save(verification);
                    return true;
                })
                .orElse(false);
    }
    
    public boolean isEmailVerified(String email) {
        return emailVerificationRepository.findByEmail(email)
                .map(EmailVerifications::getIsVerified)
                .orElse(false);
    }
    
    public void deleteVerification(String email) {
        emailVerificationRepository.deleteByEmail(email);
    }
    
    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
} 