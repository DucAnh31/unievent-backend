package com.ducanh.unievent.repository;

import com.ducanh.unievent.common.enums.EventStatus;
import com.ducanh.unievent.common.enums.RegistrationStatus;
import com.ducanh.unievent.entity.Registration;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface EventRegistrationRepository extends JpaRepository<Registration, Long> {

    public Optional<Registration> findByUser_IdAndEvent_Id(Long userId, Long eventId);

    public Optional<Registration> findByIdAndEvent_Id(Long registrationId, Long eventId);

    public Optional<Registration> findByIdAndUser_Id(Long registrationId, Long userId);

    public Long countByEvent_idAndStatus(Long eventId, RegistrationStatus status);

    @Query("""
            select r
            from Registration r
            join fetch r.event
            where r.user.id = :userId
            """)
    public List<Registration> findAllByUserId(@Param("userId") Long userId);

    @EntityGraph(attributePaths = "user")
    List<Registration> findAllByEvent_IdAndEvent_Organizer_Id(Long eventId, Long organizerId);

    public Optional<Registration> findByEvent_IdAndCheckInCode(Long eventId, String checkInCode);

    public Long countByEvent_IdAndStatus(Long eventId, RegistrationStatus status);
}
