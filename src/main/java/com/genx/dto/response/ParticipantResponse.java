package com.genx.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class ParticipantResponse {
    private Long id;
    private String fullName;
    private String identityNumber;
    private String kitCode;
    private String kitEnteredByName;
    private LocalDateTime kitEnteredAt;
}
