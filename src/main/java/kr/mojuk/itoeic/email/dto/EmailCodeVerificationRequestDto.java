package kr.mojuk.itoeic.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailCodeVerificationRequestDto {
    private String email;
    private String code;
} 