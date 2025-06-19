package com.genx.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantResponse {
    private Long id;
    private String fullName;
    private String gender;
    private String yearOfBirth;
    private String identityNumber;
    private String issueDate;
    private String issuePlace;
    private String relationship;
}