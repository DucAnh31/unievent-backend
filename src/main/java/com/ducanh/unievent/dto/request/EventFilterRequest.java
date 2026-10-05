package com.ducanh.unievent.dto.request;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.ducanh.unievent.common.enums.EventStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFilterRequest {
    @Size(max = 200, message = "Keyword must not exceed 200 character")
    private String keyword;

    @Size(max = 20, message = "Too many categories")
    private List<@Positive(message = "Category id must be positive") Long> categoryIds;

    private List<EventStatus> statuses;

    @DateTimeFormat(
            iso =
                    DateTimeFormat.ISO
                            .DATE_TIME) // cho viec chuyen doi tu url(hoac form data) vao object, createEventReq khong
    // can vi co json roi
    private Instant from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant to;
}
