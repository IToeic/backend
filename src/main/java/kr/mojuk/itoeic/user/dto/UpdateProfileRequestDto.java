package kr.mojuk.itoeic.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequestDto {
    private String currentPassword;
    private String newName;
    private String newPassword;
} 