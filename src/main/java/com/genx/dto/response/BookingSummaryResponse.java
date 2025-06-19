package com.genx.dto.response;

import com.genx.enums.EBookingStatus;
import com.genx.enums.ECaseType;
import com.genx.enums.ECollectionMethod;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingSummaryResponse {
    private Long bookingId;
    private String recordStaffName;
    private String registrantName;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private EBookingStatus status;
    private ECaseType caseType;
    private ECollectionMethod collectionMethod;
}
