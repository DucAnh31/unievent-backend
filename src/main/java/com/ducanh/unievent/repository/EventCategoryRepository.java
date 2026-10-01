package com.ducanh.unievent.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ducanh.unievent.entity.EventCategory;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {

    Page<EventCategory> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
