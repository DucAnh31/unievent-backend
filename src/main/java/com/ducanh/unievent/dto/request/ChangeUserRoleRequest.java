package com.ducanh.unievent.dto.request;

import com.ducanh.unievent.common.enums.UserRole;
import com.ducanh.unievent.common.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
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
