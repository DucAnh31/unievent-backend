package com.ducanh.unievent.entity;

import com.ducanh.unievent.common.enums.EventStatus;
import com.ducanh.unievent.common.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "registrations",
       uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_registration_user_id_event_id",
                    columnNames = {"user_id", "event_id"}
            )
       })
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RegistrationStatus status;

    @Column(name = "check_in_code", unique = true)
    private String checkInCode;

    @Column(name = "registered_at", nullable = false)
    private Instant registeredAt;


    @Column(name = "cancelled_at")
    private Instant cancelledAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
