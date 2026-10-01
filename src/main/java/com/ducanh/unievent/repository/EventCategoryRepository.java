package com.ducanh.unievent.repository;

import com.ducanh.unievent.entity.EventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
    //Page<EventCategory> findAll(Pageable pageable); // contain la LIKE
    Page<EventCategory> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
