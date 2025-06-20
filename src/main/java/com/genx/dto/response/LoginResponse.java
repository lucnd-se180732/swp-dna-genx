package com.genx.dto.response;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String username;
    private String fullName;
    private String phoneNumber;
    private String accessToken;
    private String refreshToken;
    private String email;
    private String role;
}
