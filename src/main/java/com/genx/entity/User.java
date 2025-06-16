package com.genx.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.genx.enums.ERole;
import com.genx.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = true)
    private String fullName;

    @Column(name = "gender", nullable = true)
    private String gender;

    @Column(name = "phone", unique = true, nullable = true)
    private String phone;

    @Column(unique = true, nullable = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ERole role;


    @Column(unique = true, nullable = false)
    private String username;


    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonProperty("authProvider")
    private AuthProvider authProvider;

}