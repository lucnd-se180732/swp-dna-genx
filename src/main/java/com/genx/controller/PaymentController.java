package com.genx.controller;

import com.genx.dto.response.PaymentResponse;
import com.genx.dto.response.BookingResponse;
import com.genx.entity.Payment;
import com.genx.entity.Booking;
import com.genx.enums.EPaymentStatus;
import com.genx.mapper.PaymentMapper;
import com.genx.repository.IPaymentRepository;
import com.genx.service.BookingService;
import com.genx.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/vnpay")  // Keeping original VNPay endpoint
//@CrossOrigin(origins = "*")
public class PaymentController {
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private IPaymentRepository paymentRepository;
    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private BookingService bookingService;

    @PostMapping("/create-payment")  // Keeping original endpoint
    public ResponseEntity<?> createPayment(@RequestBody BookingResponse bookingResponse, HttpServletRequest request) {
        try {
            Booking booking = bookingService.getFullRegistrationById(bookingResponse.getId());

            // Check if registration can be paid
            if (booking.getPaymentStatus() == EPaymentStatus.CANCELLED) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Booking has been cancelled"));
            }

            String paymentUrl = vnPayService.createPaymentUrl(booking, request.getRemoteAddr());
            return ResponseEntity.ok(Map.of(
                    "paymentUrl", paymentUrl,
                    "registrationId", bookingResponse.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

   // @Value("${frontendUrl}")
    private String frontendUrl = "http://localhost:3000"; // Default value, can be overridden in application.properties

    @GetMapping("/vnpay-return")
    public ResponseEntity<Void> paymentReturn(@RequestParam Map<String, String> params) {
        String orderId = params.get("vnp_TxnRef");
        PaymentResponse paymentResponse = vnPayService.validatePayment(params);

        String redirectUrl = frontendUrl + "/payment-result?success=" +
                ("00".equals(paymentResponse.getResponseCode())) +
                "&orderId=" + orderId;

        EPaymentStatus status = "00".equals(paymentResponse.getResponseCode()) ?
                EPaymentStatus.PAID : EPaymentStatus.FAILED;
        bookingService.updatePaymentStatus(orderId, status);

        return ResponseEntity.status(302).location(URI.create(redirectUrl)).build();
    }

//    @GetMapping("/vnpay-return")  // Keeping original endpoint
//    public ResponseEntity<?> paymentReturn(@RequestParam Map<String, String> params) {
//        try {
//            PaymentResponse paymentResponse = vnPayService.validatePayment(params);
//            String orderId = params.get("vnp_TxnRef");
//
//            if (paymentResponse != null && "00".equals(paymentResponse.getResponseCode())) {
//                bookingService.updatePaymentStatus(orderId, EPaymentStatus.PAID);
//                return ResponseEntity.ok(paymentResponse);
//            } else {
//                bookingService.updatePaymentStatus(orderId, EPaymentStatus.FAILED);
//                return ResponseEntity.badRequest()
//                        .body(Map.of("error", "Payment failed"));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest()
//                    .body(Map.of("error", e.getMessage()));
//        }
//    }

    @GetMapping("/payment-status/{orderId}")  // Keeping original endpoint
    public ResponseEntity<?> getPaymentStatus(@PathVariable String orderId) {
        try {
            Payment payment = paymentRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));
            return ResponseEntity.ok(paymentMapper.toDTO(payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}