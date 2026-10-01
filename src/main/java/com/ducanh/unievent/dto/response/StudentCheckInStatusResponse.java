package com.ducanh.unievent.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCheckInStatusResponse {
    private Boolean checkedIn;
    private Instant checkInTime;
}
