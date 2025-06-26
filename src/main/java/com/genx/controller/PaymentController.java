package com.genx.controller;

import com.genx.dto.response.PaymentResponse;
import com.genx.dto.response.BookingResponse;
import com.genx.entity.Payment;
import com.genx.entity.Booking;
import com.genx.enums.EPaymentStatus;
import com.genx.mapper.PaymentMapper;
import com.genx.repository.IBookingRepository;
import com.genx.repository.IPaymentRepository;
import com.genx.service.VNPayService;
import com.genx.service.interfaces.IBookingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/vnpay")  // Keeping original VNPay endpoint
public class PaymentController {
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private IPaymentRepository paymentRepository;
    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private IBookingRepository bookingRepository;

    @Autowired
    private IBookingService bookingService;

    @Value("${frontendUrl}")
    private String frontendRedirect;

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

    @GetMapping("/vnpay-return")
    public void paymentReturn(@RequestParam Map<String, String> params, HttpServletResponse response) {
        System.out.println("🔥 Đã vào callback");
        try {
            String orderId = params.get("vnp_TxnRef");

            PaymentResponse paymentResponse = vnPayService.validatePayment(params);

            if (paymentResponse != null && "00".equals(paymentResponse.getResponseCode())) {
                bookingService.updatePaymentStatus(orderId, EPaymentStatus.PAID);
            } else {
                bookingService.updatePaymentStatus(orderId, EPaymentStatus.FAILED);
            }

            String queryParams = params.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .reduce((a, b) -> a + "&" + b)
                    .orElse("");


           // String redirectUrl = frontendRedirect + "/payment-result?" + queryParams;
            String vnpTxnRef = params.get("vnp_TxnRef");
            boolean isSuccess = paymentResponse != null && "00".equals(paymentResponse.getResponseCode());
            String redirectUrl = frontendRedirect + "/payment-result"
                    + "?vnp_TxnRef=" + vnpTxnRef
                    + "&status=" + (isSuccess ? "success" : "fail");

            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendRedirect(frontendRedirect + "/payment-result?error=1");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


//    @GetMapping("/vnpay-return")  // Keeping original endpoint
//    public ResponseEntity<?> paymentReturn(@RequestParam Map<String, String> params) {
//        System.out.println("🔥 Đã vào callback");
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
//
//        } catch (Exception e) {
//            return ResponseEntity.badRequest()
//                    .body(Map.of("error", e.getMessage()));
//        }
//    }

    @GetMapping("/payment-status/{orderId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String orderId) {
        try {
            Payment payment = paymentRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            Booking booking = bookingService.getBookingByPayment(payment)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            PaymentResponse dto = paymentMapper.toDTO(payment);
            dto.setPaymentStatus(booking.getPaymentStatus().name());

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/payment-ipn")
    public ResponseEntity<String> handleIPN(@RequestParam Map<String, String> params) {
        PaymentResponse response = vnPayService.validatePayment(params);
        return response != null ? ResponseEntity.ok("OK") : ResponseEntity.badRequest().body("INVALID");
    }
}