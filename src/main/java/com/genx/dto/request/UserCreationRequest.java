package com.genx.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(
            regexp = "^(03[2-9]|08[1-9]|09[0|1|3|4|6|7|8|9]|07[0|6|7|8|9]|086)\\d{7}$",
            message = "Phone number not valid"
    )
    private String phoneNumber;

    private String fullName;

    private String gender;
}
