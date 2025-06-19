package com.genx.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "staff_info")
@Getter
@Setter
public class StaffInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID riêng, không dùng chung với User

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id-staff", nullable = false, unique = true)
    private User user;

    @Lob
    @Column(name = "fingerprint_data")
    private byte[] fingerprintData;
}
