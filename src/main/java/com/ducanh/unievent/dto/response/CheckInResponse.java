package com.ducanh.unievent.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInResponse {
    private Long id;
    private Long registrationId;
    private String studentName;
    private String studentCode;
    private Instant checkInTime;
}
