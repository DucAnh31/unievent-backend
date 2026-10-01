package com.ducanh.unievent.dto.request;

import com.ducanh.unievent.common.enums.UserRole;
import com.ducanh.unievent.common.enums.UserStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFilterRequest {
    @Size(max = 200, message = "Keyword must not exceed 200 character")
    private String keyword;
    private UserRole role;
    private UserStatus status;
}
