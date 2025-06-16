package com.genx.controller;

import com.genx.dto.PaymentDTO;
import com.genx.dto.RegistrationDTO;
import com.genx.entity.Payment;
import com.genx.entity.Registration;
import com.genx.enums.EPaymentStatus;
import com.genx.mapper.PaymentMapper;
import com.genx.mapper.RegistrationMapper;
import com.genx.repository.IPaymentRepository;
import com.genx.service.RegistrationService;
import com.genx.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/vnpay")  // Keeping original VNPay endpoint
@CrossOrigin(origins = "*")
public class PaymentController {
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private IPaymentRepository paymentRepository;
    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/create-payment")  // Keeping original endpoint
    public ResponseEntity<?> createPayment(@RequestBody RegistrationDTO registrationDTO,
                                           HttpServletRequest request) {
        try {
            Registration registration = registrationService.getFullRegistrationById(registrationDTO.getId());

            // Check if registration can be paid
            if (registration.getPaymentStatus() == EPaymentStatus.CANCELLED) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Registration has been cancelled"));
            }

            String paymentUrl = vnPayService.createPaymentUrl(registration, request.getRemoteAddr());
            return ResponseEntity.ok(Map.of(
                    "paymentUrl", paymentUrl,
                    "registrationId", registrationDTO.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/vnpay-return")  // Keeping original endpoint
    public ResponseEntity<?> paymentReturn(@RequestParam Map<String, String> params) {
        try {
            PaymentDTO paymentDTO = vnPayService.validatePayment(params);
            String orderId = params.get("vnp_TxnRef");

            if (paymentDTO != null && "00".equals(paymentDTO.getResponseCode())) {
                registrationService.updatePaymentStatus(orderId, EPaymentStatus.PAID);
                return ResponseEntity.ok(paymentDTO);
            } else {
                registrationService.updatePaymentStatus(orderId, EPaymentStatus.FAILED);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Payment failed"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

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