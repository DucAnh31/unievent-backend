package com.ducanh.unievent.common.util;

import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;

public final class PageableFactoryUtil {

    public static Pageable create(int page, int size, String sort, Set<String> allowedSortFields) {
        if (page < 0 || size < 1 || size > 100 || sort == null) throw new ApiException(ErrorCode.VALIDATION_ERROR);

        StringTokenizer stringTokenizer = new StringTokenizer(sort, ",");
        ArrayList<String> parts = new ArrayList<>();
        while (stringTokenizer.hasMoreTokens()) parts.add(stringTokenizer.nextToken());

        if (parts.size() != 2) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR);
        }

        String field = parts.get(0).trim();

        if (!allowedSortFields.contains(field)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR);
        }

        Sort.Direction direction = Sort.Direction.fromOptionalString(
                        parts.get(1).trim())
                .orElseThrow(() -> new ApiException(ErrorCode.VALIDATION_ERROR));

        Sort ordering = Sort.by(new Sort.Order(direction, field));

        if (!"id".equals(field)) ordering = ordering.and(Sort.by(Sort.Order.asc("id")));

        return PageRequest.of(page, size, ordering);
    }
}
