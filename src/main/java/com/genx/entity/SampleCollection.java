package com.genx.entity;

import com.genx.enums.ESampleCollectionStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sample_collections")
@Data
@Getter
@Setter
public class SampleCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "collected_by")
    private User collectedBy;

    private LocalDateTime collectedAt;

    private LocalDateTime confirmedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ESampleCollectionStatus status;

    @Column(length = 500)
    private String note;
}
