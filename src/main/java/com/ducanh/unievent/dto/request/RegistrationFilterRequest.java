package com.ducanh.unievent.dto.request;

import java.util.List;

import jakarta.validation.constraints.Size;

import com.ducanh.unievent.common.enums.RegistrationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationFilterRequest {
    @Size(max = 200, message = "Keyword must not exceed 100 characters")
    private String keyword;

    private List<RegistrationStatus> statuses;
}
