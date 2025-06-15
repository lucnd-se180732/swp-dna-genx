package com.genx.dto.response;


import com.genx.enums.EBookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BookingResponse {
    private Long bookingId;
    private String registrantName;
    private String phoneNumber;
    private String identityNumber;
    private int numberOfParticipants;
    private Long customerId;
    private Long recordStaffId;
    private String recordStaffName;
    private Long serviceId;
    private String serviceName;
    private BigDecimal servicePrice;
    private Long collectionOptionId;
    private String collectionOptionName;
    private EBookingStatus status;
    private String note;
    private LocalDateTime createdAt;
    private List<ParticipantResponse> participants;
}
