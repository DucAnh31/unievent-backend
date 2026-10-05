package com.ducanh.unievent.repository;

import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;

import com.ducanh.unievent.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    @EntityGraph(attributePaths = {"category", "organizer"})
    public Optional<Event> findByIdAndOrganizerId(Long eventId, Long organizerId);

    public Boolean existsByCategory_Id(Long categoryId);

    @EntityGraph(attributePaths = {"category", "organizer"})
    @Override
    Page<Event> findAll(Specification<Event> spec, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Event> findForRegistrationById(Long eventId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Event> findForRegistrationByIdAndOrganizer_Id(Long eventId, Long organizerId);

    @Override
    @EntityGraph(attributePaths = {"category", "organizer"})
    Optional<Event> findById(Long eventId);
}
