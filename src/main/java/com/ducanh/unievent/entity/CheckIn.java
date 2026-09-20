package com.ducanh.unievent.entity;

import com.ducanh.unievent.common.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "check_ins")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "check_in_time", nullable = false)
    private Instant checkInTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private Registration registration;
}
