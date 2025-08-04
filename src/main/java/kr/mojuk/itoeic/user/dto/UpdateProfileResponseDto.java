package kr.mojuk.itoeic.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateProfileResponseDto {
    private boolean success;
    private String message;
} 