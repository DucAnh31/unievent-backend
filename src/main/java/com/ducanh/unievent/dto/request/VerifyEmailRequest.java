package com.ducanh.unievent.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyEmailRequest {
    @NotBlank(message = "Token not blank")
    private String token;
}
