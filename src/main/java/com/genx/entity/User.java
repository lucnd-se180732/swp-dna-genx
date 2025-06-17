package com.genx.entity;

import com.genx.enums.ERole;
import com.genx.enums.EAuthProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {


    @Column(name = "phone", unique = true, nullable = true)
    private String phone;

    @Column(unique = true, nullable = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ERole role;

    // Username bắt buộc, dùng cho đăng nhập hệ thống
    @Column(unique = true, nullable = false)
    private String username;

    //fullname
    private String fullName;

    //gneder
    private boolean gender;

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
    private EAuthProvider authProvider;
}
