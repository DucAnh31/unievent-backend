package com.ducanh.unievent.dto.response;

import java.time.Instant;

import com.ducanh.unievent.common.enums.RegistrationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String eventTitle;
    private Long eventId;

    private String studentName;
    private String studentCode;
    private Long userId;
}
