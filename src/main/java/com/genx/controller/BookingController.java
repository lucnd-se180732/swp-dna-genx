package com.genx.controller;


import com.genx.dto.request.BookingRequest;
import com.genx.dto.response.AdnResultResponse;
import com.genx.dto.response.BookingResponse;
import com.genx.entity.AdnResult;
import com.genx.enums.EPaymentStatus;

import com.genx.repository.IAdnResultRepository;
import com.genx.service.interfaces.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/registrations")
public class BookingController {
    @Autowired
    private IBookingService bookingService;

    @Autowired
    private IAdnResultRepository adnResultRepository;

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
    @GetMapping("/lookup")
    public ResponseEntity<AdnResultResponse> lookupResult(
            @RequestParam String trackingCode,
            @RequestParam String trackingPassword) {

        AdnResult result = adnResultRepository.findByTrackingCodeAndTrackingPassword(trackingCode, trackingPassword)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kết quả"));

        AdnResultResponse response = new AdnResultResponse();
        response.setTrackingCode(result.getTrackingCode());
        response.setTrackingPassword(result.getTrackingPassword());
        response.setConclusion(result.getConclusion());
        response.setLociResults(result.getLociResults());

        return ResponseEntity.ok(response);
    }

}
