package com.ducanh.unievent.repository;

import com.ducanh.unievent.entity.EventCategory;
import com.ducanh.unievent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
}
