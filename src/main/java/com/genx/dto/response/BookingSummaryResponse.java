package com.genx.dto.response;

import com.genx.enums.EBookingStatus;
import com.genx.enums.ECaseType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingSummaryResponse {
    private Long id;
    private String customerName;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private EBookingStatus status;
    private ECaseType caseType;
}
