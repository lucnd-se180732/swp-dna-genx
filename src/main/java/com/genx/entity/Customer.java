package com.genx.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(nullable = true, length = 255)
    private String address;



    @Column(nullable = true, length = 255)
    private String avatar;

    @Column(name = "dob", nullable = true)
    private LocalDate dob;
}