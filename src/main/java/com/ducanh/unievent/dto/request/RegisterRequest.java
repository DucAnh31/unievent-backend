package com.ducanh.unievent.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Full name must not be blank")
    @Size(max = 200, message = "Full name must not exceed 200 characters")
    private String fullName;

    @NotBlank(message = "Username must not be blank")
    @Size(max = 200, message = "Username must not exceed 200 characters")
    private String username;

    @NotBlank(message = "Email must not be blank")
    @Size(max = 200, message = "Email must not exceed 200 characters")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;

    @Size(max = 50, message = "Student code must not exceed 50 characters")
    private String studentCode;
}
