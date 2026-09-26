package com.ducanh.unievent.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    private List<T> content;
    private Integer page;
    private Integer size;
    private Long totalElement;
    private Integer totalPage;
    private Boolean hasNext;

    public static <T> PageResponse<T> from(Page<T> result)
    {
        return PageResponse.<T>builder()
                .content(result.getContent())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElement(result.getTotalElements())
                .totalPage(result.getTotalPages())
                .hasNext(result.hasNext())
                .build();
    }
}
