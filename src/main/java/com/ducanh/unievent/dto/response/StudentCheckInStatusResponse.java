package com.ducanh.unievent.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCheckInStatusResponse {
    private Boolean checkedIn;
    private Instant checkInTime;
}
