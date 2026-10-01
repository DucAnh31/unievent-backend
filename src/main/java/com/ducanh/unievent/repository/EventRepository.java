package com.ducanh.unievent.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.ducanh.unievent.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    @Query(
            """
			select e
			from Event e
			join fetch e.organizer
			join fetch e.category
			where e.organizer.id = :organizerId
			""")
    public List<Event> findAllByOrganizerId(@Param("organizerId") Long organizerId);

    public Optional<Event> findByIdAndOrganizerId(
            @Param("eventId") Long eventId, @Param(("organizer")) Long organizerId);

    public Boolean existsByCategory_Id(Long categoryId);

    @EntityGraph(attributePaths = {"category", "organizer"})
    @Override
    Page<Event> findAll(Specification<Event> spec, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Event> findForRegistrationById(Long eventId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Event> findForRegistrationByIdAndOrganizer_Id(Long eventId, Long organizerId);
}
