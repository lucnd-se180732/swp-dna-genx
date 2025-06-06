package com.genx.dto;

import com.genx.enums.UserRole;
import com.genx.enums.StaffType;
import com.genx.enums.Status;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String displayName;
    private String avatar;
    private UserRole role;
    private StaffType staffType;
    private Status status;
}