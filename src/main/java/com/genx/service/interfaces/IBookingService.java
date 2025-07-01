package com.genx.service.interfaces;

import com.genx.dto.request.KitCodeRequest;
import com.genx.dto.response.BookingResponse;
import com.genx.dto.response.BookingSummaryResponse;
import com.genx.enums.EBookingStatus;
import com.genx.enums.EPaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IBookingService {
    Page<BookingResponse> getAllBookings(Pageable pageable);
    Page<BookingSummaryResponse> searchBookingSummaries(EBookingStatus status, Long id, Pageable pageable);
    BookingResponse getBookingById(Long id);
    BookingResponse confirmBooking(Long id);
    BookingResponse cancelBooking(Long id, String reason);
    Page<BookingResponse> searchBookings(EBookingStatus status, Long id, Pageable pageable);
   // BookingResponse enterKitCodes(Long id, List<KitCodeRequest> kitCodes);

    Optional<Long> getTodayRevenue(EPaymentStatus status);

    Optional<Long> getMonthlyRevenue(EPaymentStatus status, int month, int year);

    long countByPaymentStatus(EPaymentStatus status);
}