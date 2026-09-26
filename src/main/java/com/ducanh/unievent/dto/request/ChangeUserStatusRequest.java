package com.ducanh.unievent.dto.request;

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
public class ChangeUserStatusRequest {
    @NotNull(message = "Status must not be null")
    private UserStatus status;
}
