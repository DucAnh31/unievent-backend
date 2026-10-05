package com.ducanh.unievent.entity;

import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;

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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private Registration registration;
}
