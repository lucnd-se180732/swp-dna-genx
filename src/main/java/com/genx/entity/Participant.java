package com.genx.entity;

import com.genx.enums.EParticipantSampleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "participants")
@Getter
@Setter
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(name = "identity_number", nullable = false, length = 100)
    private String identityNumber;

    @Column(name = "kit_code", length = 100)
    private String kitCode;

    @ManyToOne
    @JoinColumn(name = "kit_entered_by")
    private User kitEnteredBy;

    private LocalDateTime kitEnteredAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "sample_status")
    private EParticipantSampleStatus sampleStatus;

}
