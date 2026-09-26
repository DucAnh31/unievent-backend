package com.ducanh.unievent.dto.request;

import com.ducanh.unievent.common.enums.EventStatus;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Instant;
import java.util.List;
import java.util.Set;

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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // cho viec chuyen doi tu url vao object, createEventReq khong can vi co json roi
    private Instant from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant to;
}
