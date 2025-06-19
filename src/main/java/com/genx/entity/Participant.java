package com.genx.entity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "participant")
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

}