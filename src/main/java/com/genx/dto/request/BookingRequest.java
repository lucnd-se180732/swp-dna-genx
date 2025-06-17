package com.genx.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class BookingRequest {
    private String registrantName;
    private String phoneNumber;
    private String identityNumber;
    private int numberOfParticipants;
    private Long customerId;
    private Long recordStaffId;
    private Long serviceId;
    private List<ParticipantRequest> participants;
}
