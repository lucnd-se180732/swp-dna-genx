package com.genx.entity;
import com.genx.enums.StaffType;
import com.genx.enums.Status;
import com.genx.enums.UserRole;
import jakarta.persistence.*;

import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column( length = 255)
    private String avatar;

    @Column(nullable = false, length = 50 )
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column( length = 100)
    @Enumerated(EnumType.STRING)
    private StaffType staffType;

    @Column(nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private Status status;

}
