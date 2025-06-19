package com.genx.entity;

import com.genx.enums.EParticipantSampleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "participant")
@Getter
@Setter
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_booking", nullable = false)
    private Booking booking;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(name = "identity_number", nullable = false, length = 100)
    private String identityNumber;

    @Column(name = "issue_date")
    private String issueDate;

    @Column(name = "issue_place")
    private String issuePlace;

    @Column(name = "year_of_birth")
    private String yearOfBirth;

    @Column(name = "kit_code", length = 100)
    private String kitCode;

    @ManyToOne
    @JoinColumn(name = "kit_entered_by")
    private StaffInfo kitEnteredBy;

    private LocalDateTime kitEnteredAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "sample_status")
    private EParticipantSampleStatus sampleStatus = EParticipantSampleStatus.PENDING;

}
