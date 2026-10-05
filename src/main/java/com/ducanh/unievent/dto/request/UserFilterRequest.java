package com.ducanh.unievent.dto.request;

import jakarta.validation.constraints.Size;

import com.ducanh.unievent.common.enums.UserRole;
import com.ducanh.unievent.common.enums.UserStatus;

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
