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
public class EventRegistrationResponse {
    private Long id;
    private RegistrationStatus status;
    private String checkInCode;


    private Instant registeredAt;
    private Instant cancelledAt;

    private Long eventId;
    private Long userId;
}
