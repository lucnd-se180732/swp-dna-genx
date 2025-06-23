package com.genx.controller;

import com.genx.dto.request.KitCodeRequest;
import com.genx.dto.response.BookingResponse;
import com.genx.dto.response.BookingSummaryResponse;
import com.genx.enums.EBookingStatus;
import com.genx.service.interfaces.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff/booking")
public class StaffBookingController {

    @Autowired
    private IBookingService bookingService;

    @GetMapping
    public Page<BookingSummaryResponse> searchBookings(
            @RequestParam(required = false) EBookingStatus status,
            @RequestParam(required = false) Long bookingId,
            Pageable pageable
    ) {
        return bookingService.searchBookingSummaries(status, bookingId, pageable);
    }

    @GetMapping("/all")
    public Page<BookingResponse> searchAllBookings(
            @RequestParam(required = false) EBookingStatus status,
            @RequestParam(required = false) Long bookingId,
            Pageable pageable
    ) {
        return bookingService.searchBookings(status, bookingId, pageable);
    }

    @GetMapping("/{id}")
    public BookingResponse getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @PutMapping("/{id}/confirm")
    public BookingResponse confirmBooking(@PathVariable Long id) {
        return bookingService.confirmBooking(id);
    }



}
