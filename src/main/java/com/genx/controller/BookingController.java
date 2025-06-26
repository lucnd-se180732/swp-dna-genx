package com.genx.controller;


import com.genx.dto.request.BookingRequest;
import com.genx.dto.response.BookingResponse;
import com.genx.enums.EPaymentStatus;

import com.genx.service.interfaces.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/registrations")
//@CrossOrigin(origins = "*")
public class BookingController {
    @Autowired
    private IBookingService bookingService;

  // In BookingController.java
  @PostMapping("/register")
  public ResponseEntity<BookingResponse> createRegistration(@RequestBody BookingRequest bookingRequest) {
      BookingResponse savedRegistration = bookingService.createRegistration(bookingRequest);
      return ResponseEntity.ok(savedRegistration);
  }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getRegistration(@PathVariable Long id) {
        BookingResponse registration = bookingService.getBookingById(id);
        return ResponseEntity.ok(registration);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelRegistration(@PathVariable Long id) {
        BookingResponse cancelledRegistration = bookingService.cancelRegistration(id);
        return ResponseEntity.ok(cancelledRegistration);
    }
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllRegistrations(
            @RequestParam(required = false) EPaymentStatus status) {
        List<BookingResponse> registrations;
        if (status != null) {
            registrations = bookingService.getRegistrationsByStatus(status);
        } else {
            registrations = bookingService.getAllRegistrations();
        }
        return ResponseEntity.ok(registrations);
    }

}
