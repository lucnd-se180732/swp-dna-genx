package com.genx.controller;


import com.genx.dto.PaymentDTO;
import com.genx.dto.RegistrationDTO;
import com.genx.entity.Registration;
import com.genx.enums.PaymentStatus;
import com.genx.mapper.RegistrationMapper;
import com.genx.service.RegistrationService;
import com.genx.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/registrations")
@CrossOrigin(origins = "*")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private RegistrationMapper registrationMapper;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody RegistrationDTO registrationDTO,
                                      HttpServletRequest request) {
        try {
            // Bước 1: Gán trạng thái UNPAID
            registrationDTO.setPaymentStatus(PaymentStatus.UNPAID);

            // Bước 2: Gọi service để tạo đơn đăng ký
            RegistrationDTO savedRegistration = registrationService.createRegistration(registrationDTO);

            // Bước 3: Lấy lại bản ghi registration đã có đầy đủ thông tin (bao gồm Service)
            Registration registration = registrationService.getFullRegistrationById(savedRegistration.getId());

            // Bước 4: Tạo link thanh toán
            String paymentUrl = vnPayService.createPaymentUrl(registration, request.getRemoteAddr());

            // Bước 5: Trả về kết quả cho client
            Map<String, Object> response = new HashMap<>();
            response.put("registrationId", savedRegistration.getId());
            response.put("paymentUrl", paymentUrl);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


    @GetMapping("/payment-result")
    public String paymentResult(HttpServletRequest request, Model model) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));

        PaymentDTO paymentDTO = vnPayService.validatePayment(params);
        String orderId = request.getParameter("vnp_TxnRef");

        if (paymentDTO != null && "00".equals(paymentDTO.getResponseCode())) {
            registrationService.updatePaymentStatus(orderId, PaymentStatus.PAID);
            model.addAttribute("message", "Payment successful!");
        } else {
            registrationService.updatePaymentStatus(orderId, PaymentStatus.FAILED);
            model.addAttribute("message", "Payment failed!");
        }

        return "payment-result";
    }
}
