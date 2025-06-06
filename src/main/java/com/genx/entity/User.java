package com.genx.entity;


import com.genx.enums.AuthProvider;
import com.genx.enums.ERole;
import jakarta.persistence.*;
import lombok.*;


@Builder
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    // Email có thể null nếu user đăng ký bằng username/password
    @Column(unique = true, nullable = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ERole role;

    // Username bắt buộc, dùng cho đăng nhập hệ thống
    @Column(unique = true, nullable = false)
    private String username;

    // Password có thể null nếu đăng nhập bằng Gmail OAuth2
    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;

    // Trường đánh dấu loại tài khoản, ví dụ: SYSTEM / GOOGLE
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

}
