package com.genx.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateProfileRequest {
    private String fullName;
    private String phoneNumber;
    private String gender;
    private String avatar;

    private LocalDateTime startDate;
}
