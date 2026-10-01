package com.ducanh.unievent.specification;

import com.ducanh.unievent.common.enums.EventStatus;
import com.ducanh.unievent.dto.request.EventFilterRequest;
import com.ducanh.unievent.entity.Event;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;

public class EventSpecification {

    public static Specification<Event> buildEventSpecification(EventFilterRequest filter) {
        return EventSpecification.isPublic()
                .and(hasStatus(filter.getStatuses()))
                .and(hasCategoryId(filter.getCategoryIds()))
                .and(hasKeyword(filter.getKeyword()))
                .and(startFrom(filter.getFrom()))
                .and(startBefore(filter.getTo()));
    }

    public static Specification<Event> isPublic()
    {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.notEqual(root.get("status"), EventStatus.DRAFT);
    }

    public static Specification<Event> hasStatus(List<EventStatus> statuses)
    {
        return (root, query, criteriaBuilder)
                -> (statuses == null || statuses.isEmpty())
                    ? criteriaBuilder.conjunction()
                    : root.get("status").in(statuses);
    }

    public static Specification<Event> hasCategoryId(List<Long> categoryIds)
    {
        return (root, query, criteriaBuilder)
                -> (categoryIds == null || categoryIds.isEmpty())
                    ? criteriaBuilder.conjunction()
                    : root.get("category").get("id").in(categoryIds);
    }

    public static Specification<Event> hasKeyword(String keyword)
    {
        return (root, query, criteriaBuilder)
                ->
                {
                    if(keyword == null || keyword.isBlank())
                        return criteriaBuilder.conjunction();

                    String escaped = keyword.trim()
                            .toLowerCase()
                            .replace("!", "!!")
                            .replace("%", "!%")
                            .replace("_", "!_");

                    String pattern = "%" + escaped + "%";

                    return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern, '!');
                };
    }

    public static Specification<Event> startFrom(Instant from) {
        return (root, query, criteriaBuilder)
                -> from == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), from);
    }

    public static Specification<Event> startBefore(Instant to) {
        return (root, query, criteriaBuilder)
                -> to == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.lessThanOrEqualTo(root.get("startTime"), to);
    }



}
