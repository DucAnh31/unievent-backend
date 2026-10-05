package com.ducanh.unievent.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ducanh.unievent.common.enums.RegistrationStatus;
import com.ducanh.unievent.dto.request.RegistrationFilterRequest;
import com.ducanh.unievent.entity.Registration;

public class RegistrationSpecification {

    public static Specification<Registration> buildRegistrationSpecification(
            Long eventId, RegistrationFilterRequest filter) {
        return hasEventId(eventId).and(hasStatus(filter.getStatuses())).and(hasStudentKeyword(filter.getKeyword()));
    }

    private static Specification<Registration> hasEventId(Long eventId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("event").get("id"), eventId);
    }

    private static Specification<Registration> hasStatus(List<RegistrationStatus> statuses) {
        return (root, query, criteriaBuilder) -> statuses == null || statuses.isEmpty()
                ? criteriaBuilder.conjunction()
                : root.get("status").in(statuses);
    }

    private static Specification<Registration> hasStudentKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String escaped = keyword.trim()
                    .toLowerCase()
                    .replace("!", "!!")
                    .replace("%", "!%")
                    .replace("_", "!_");

            String pattern = "%" + escaped + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("user").get("fullName")), pattern, '!'),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("user").get("studentCode")), pattern, '!'));
        };
    }
}
