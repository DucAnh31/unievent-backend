package com.ducanh.unievent.dto.request;

import jakarta.validation.constraints.NotNull;

import com.ducanh.unievent.common.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeUserRoleRequest {
    @NotNull(message = "Role must not be null")
    private UserRole role;
}
