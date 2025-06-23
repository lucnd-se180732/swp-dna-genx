package com.genx.dto.response;

import com.genx.enums.ERole;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String phone;
    private String fullName;
    private String gender;
    private String email;
    private String username;
    private ERole role;
}
