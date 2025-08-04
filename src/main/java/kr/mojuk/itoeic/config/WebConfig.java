package kr.mojuk.itoeic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor())
                .addPathPatterns("/api/user/mypage/**")
                .excludePathPatterns("/api/user/login", "/api/user/signup", "/api/user/send-verification-email", "/api/user/verify-email-code");
    }
} 