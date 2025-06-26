package com.genx.service.interfaces;

import com.genx.dto.request.BookingRequest;
import com.genx.dto.response.BookingResponse;
import com.genx.dto.response.BookingSummaryResponse;
import com.genx.entity.Booking;
import com.genx.entity.Payment;
import com.genx.enums.EBookingStatus;
import com.genx.enums.EPaymentStatus;
import com.genx.enums.ESampleCollectionStatus;
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

    Booking getFullRegistrationById(Long id);

    List<BookingResponse> getRegistrationsByStatus(EPaymentStatus status);

    List<BookingResponse> getAllApplicationsSentToLab();
    List<BookingResponse> getAllCompletedApplications();

    BookingResponse createRegistration(BookingRequest bookingRequest);

    BookingResponse updateRegistration(Long id, BookingRequest bookingRequest);

    BookingResponse cancelRegistration(Long id);

    List<BookingResponse> getAllRegistrations();

    BookingResponse updatePaymentStatus(String orderId, EPaymentStatus status);

    Optional<Booking> getBookingByPayment(Payment payment);

    Page<BookingResponse> searchBySampleStatus(
            ESampleCollectionStatus status,
            String code,
            Pageable pageable
    );


}