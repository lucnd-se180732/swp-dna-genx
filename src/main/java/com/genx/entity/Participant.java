package com.genx.entity;

import com.genx.enums.EParticipantSampleStatus;
import com.genx.enums.ESampleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "participant")
@Getter
@Setter
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "year_of_birth")
    private String yearOfBirth;

    @Column(name = "identity_number")
    private String identityNumber;

    @Column(name = "issue_date")
    private String issueDate;

    @Column(name = "issue_place")
    private String issuePlace;

    @Column(name = "relationship")
    private String relationship;
    
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(name = "kit_code", length = 100)
    private String kitCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kit_entered_by")
    private StaffInfo kitEnteredBy;

    private LocalDateTime kitEnteredAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "sample_status")
    private EParticipantSampleStatus sampleStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "sample_type")
    private ESampleType sampleType;

    @Lob
    private byte[] fingerprintData;


}