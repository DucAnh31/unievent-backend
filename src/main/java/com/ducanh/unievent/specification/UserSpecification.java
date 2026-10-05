package com.ducanh.unievent.specification;

import org.springframework.data.jpa.domain.Specification;

import com.ducanh.unievent.common.enums.UserRole;
import com.ducanh.unievent.common.enums.UserStatus;
import com.ducanh.unievent.dto.request.UserFilterRequest;
import com.ducanh.unievent.entity.User;

public class UserSpecification {
    public static Specification<User> buildUserSpecification(UserFilterRequest request) {
        return hasKeyword(request.getKeyword()).and(hasRole(request.getRole())).and(hasStatus(request.getStatus()));
    }

    public static Specification<User> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank()) return criteriaBuilder.conjunction();

            String escaped = keyword.trim()
                    .toLowerCase()
                    .replace("!", "!!")
                    .replace("%", "!%")
                    .replace("_", "!_");

            String pattern = "%" + escaped + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), pattern, '!'),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), pattern, '!'),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), pattern, '!'),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("studentCode")), pattern, '!'));
        };
    }

    public static Specification<User> hasStatus(UserStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) return criteriaBuilder.conjunction();

            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<User> hasRole(UserRole role) {
        return (root, query, criteriaBuilder) -> {
            if (role == null) return criteriaBuilder.conjunction();

            return criteriaBuilder.equal(root.get("role"), role);
        };
    }
}
