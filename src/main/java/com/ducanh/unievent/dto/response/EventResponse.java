package com.ducanh.unievent.dto.response;

import java.time.Instant;

import com.ducanh.unievent.common.enums.EventStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Instant startTime;
    private Instant endTime;
    private Instant registrationDeadline;
    private Integer maxParticipants;
    private EventStatus status;
    private Long categoryId;
    private String categoryName;
    private Long organizerId;
    private String organizerName;
    private Instant createdAt;
    private Instant updatedAt;
}
