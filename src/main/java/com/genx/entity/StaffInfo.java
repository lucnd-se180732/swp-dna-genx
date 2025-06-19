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
    private Long id;  // Chính là id trong bảng User

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Lob
    private byte[] fingerprintData;

}
