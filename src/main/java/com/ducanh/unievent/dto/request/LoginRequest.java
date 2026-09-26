package com.ducanh.unievent.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "Identifier must not be blank")
    private String identifier;

    @NotBlank(message = "Password must not be blank")
    private String password;
}
