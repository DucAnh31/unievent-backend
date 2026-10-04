package com.ducanh.unievent.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ducanh.unievent.common.enums.RegistrationStatus;
import com.ducanh.unievent.entity.Registration;

public interface EventRegistrationRepository
        extends JpaRepository<Registration, Long>, JpaSpecificationExecutor<Registration> {

    public Optional<Registration> findByUser_IdAndEvent_Id(Long userId, Long eventId);

    public Optional<Registration> findByIdAndEvent_Id(Long registrationId, Long eventId);

    public Optional<Registration> findByIdAndUser_Id(Long registrationId, Long userId);

    public Long countByEvent_idAndStatus(Long eventId, RegistrationStatus status);

    @EntityGraph(attributePaths = {"user", "event"})
    public Page<Registration> findAllByUser_Id(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = "user")
    List<Registration> findAllByEvent_IdAndEvent_Organizer_Id(Long eventId, Long organizerId);

    public Optional<Registration> findByEvent_IdAndCheckInCode(Long eventId, String checkInCode);

    @EntityGraph(attributePaths = {"user", "event"})
    @Override
    Page<Registration> findAll(Specification<Registration> spec, Pageable pageable);
}
