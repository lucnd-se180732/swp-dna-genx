package com.genx.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id-customer", nullable = false, unique = true)
    private User user;

    @Column(length = 255)
    private String address;

    @Column(length = 255)
    private String avatar;

    @Column(name = "dob")
    private LocalDate dob;
}
