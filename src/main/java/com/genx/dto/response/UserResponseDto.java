package com.genx.dto.response;

import com.genx.enums.ERole;
import com.genx.enums.AuthProvider;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String fullName;
    private String gender;
    private String phone;
    private String email;
    private String username;
    private ERole role;
    private boolean enabled;
    private boolean accountNonLocked;
    private AuthProvider authProvider;
}