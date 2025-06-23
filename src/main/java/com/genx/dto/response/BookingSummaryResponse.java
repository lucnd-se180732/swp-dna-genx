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
    private Long id;
    private String code; // Thêm mã đơn
    private String customerName;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private EBookingStatus status;
    private ECaseType caseType;
    private String serviceTypeName;         // Dịch vụ
    private ECollectionMethod collectionMethod; // Hình thức lấy mẫu
}
