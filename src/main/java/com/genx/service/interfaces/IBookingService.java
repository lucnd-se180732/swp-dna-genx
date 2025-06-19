package com.genx.service.interfaces;

import com.genx.dto.request.KitCodeRequest;
import com.genx.dto.response.BookingResponse;
import com.genx.dto.response.BookingSummaryResponse;
import com.genx.enums.EBookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBookingService {
    Page<BookingResponse> getAllBookings(Pageable pageable);
    Page<BookingSummaryResponse> searchBookingSummaries(EBookingStatus status, Long bookingId, Pageable pageable);
    BookingResponse getBookingById(Long id);
    BookingResponse confirmBooking(Long id);
    BookingResponse cancelBooking(Long id, String reason);
    Page<BookingResponse> searchBookings(EBookingStatus status, Long bookingId, Pageable pageable);
   // BookingResponse enterKitCodes(Long bookingId, List<KitCodeRequest> kitCodes);

}