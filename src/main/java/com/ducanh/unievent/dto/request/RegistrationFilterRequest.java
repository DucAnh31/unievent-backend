package com.ducanh.unievent.dto.request;

import com.ducanh.unievent.common.enums.RegistrationStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationFilterRequest {
    @Size(max = 200, message = "Keyword must not exceed 100 characters")
    private String keyword;

    private List<RegistrationStatus> statuses;
}
