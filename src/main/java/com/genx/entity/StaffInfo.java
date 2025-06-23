package com.genx.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "staff_info")
@Getter
@Setter
public class StaffInfo {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Lob
    private byte[] fingerprintData;

    @Column(length = 100)
    private String avatar;

    @Column(length = 100)
    private LocalDateTime startdDate;
}