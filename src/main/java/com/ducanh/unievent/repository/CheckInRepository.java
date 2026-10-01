package com.ducanh.unievent.repository;

import com.ducanh.unievent.entity.CheckIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    public Optional<CheckIn> findByRegistration_Id(Long registrationId);

    @EntityGraph(attributePaths = "registration.user")
    public Page<CheckIn> findByRegistration_Event_Id(Long eventId, Pageable pageable);

    public Long countByRegistration_Event_Id(Long eventId);
}
