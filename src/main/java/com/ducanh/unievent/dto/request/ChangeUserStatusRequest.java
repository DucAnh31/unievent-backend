package com.ducanh.unievent.dto.request;

import jakarta.validation.constraints.NotNull;

import com.ducanh.unievent.common.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeUserStatusRequest {
    @NotNull(message = "Status must not be null")
    private UserStatus status;
}
