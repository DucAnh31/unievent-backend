package com.ducanh.unievent.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRequest {
    @NotBlank(message = "Check-in code must not be blank")
    @Size(max = 100, message = "Invalid check-in code")
    private String checkInCode;
}
