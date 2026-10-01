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
    private Long eventId;
    private Instant registeredAt;
    private Instant cancelledAt;
}
