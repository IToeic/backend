package kr.mojuk.itoeic.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyPageResponseDto {
    private boolean success;
    private String message;
    private String userId;
    private String name;
    private String email;
} 