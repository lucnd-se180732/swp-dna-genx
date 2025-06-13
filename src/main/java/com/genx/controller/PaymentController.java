package com.genx.controller;

import com.genx.dto.PaymentDTO;
import com.genx.dto.RegistrationDTO;
import com.genx.entity.Payment;
import com.genx.entity.Registration;
import com.genx.mapper.PaymentMapper;
import com.genx.mapper.RegistrationMapper;
import com.genx.repository.PaymentRepository;
import com.genx.service.RegistrationService;
import com.genx.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vnpay")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private RegistrationMapper registrationMapper;

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody RegistrationDTO registrationDTO,
                                           HttpServletRequest request) {
        try {
            Registration registration = registrationMapper.toEntity(registrationDTO);
            String paymentUrl = vnPayService.createPaymentUrl(registration, request.getRemoteAddr());
            if (paymentUrl == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Error creating payment URL"));
            }
            return ResponseEntity.ok(Map.of(
                    "paymentUrl", paymentUrl,
                    "registrationId", registrationDTO.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> paymentReturn(@RequestParam Map<String, String> params) {
        try {
            PaymentDTO paymentDTO = vnPayService.validatePayment(params);
            if (paymentDTO != null && "00".equals(paymentDTO.getResponseCode())) {
                return ResponseEntity.ok(paymentDTO);
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Payment failed"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/payment-status/{orderId}")
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