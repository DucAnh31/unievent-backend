package com.ducanh.unievent.dto.response;

import com.ducanh.unievent.common.enums.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizerRegistrationResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private String studentCode;

    private RegistrationStatus status;
    private Instant registeredAt;
    private Instant cancelledAt;
}
