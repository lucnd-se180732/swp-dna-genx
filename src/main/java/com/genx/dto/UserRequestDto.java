package com.genx.dto;

import com.genx.enums.AuthProvider;
import com.genx.enums.ERole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {
    private String fullName;
    private String gender;
    private String phone;
    private String email;
    private String username;
    private String password;
    private ERole role;
    private AuthProvider authProvider;
}