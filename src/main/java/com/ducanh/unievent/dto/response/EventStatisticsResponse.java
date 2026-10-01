package com.ducanh.unievent.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventStatisticsResponse {
    private Long eventId;
    private Integer maxParticipants;
    private Long registeredCount;
    private Long checkedInCount;
}
