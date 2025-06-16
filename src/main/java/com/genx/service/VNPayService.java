package com.genx.service;


import com.genx.config.VNPayConfig;
import com.genx.dto.PaymentDTO;
import com.genx.entity.Payment;
import com.genx.entity.Registration;
import com.genx.enums.EPaymentStatus;
import com.genx.mapper.PaymentMapper;
import com.genx.repository.IPaymentRepository;
import com.genx.repository.IRegistrationRepository;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class VNPayService {
    private final VNPayConfig vnPayConfig;
    private final IRegistrationRepository IRegistrationRepository;
    private final IPaymentRepository IPaymentRepository;
    private final PaymentMapper paymentMapper;

    @Autowired
    public VNPayService(VNPayConfig vnPayConfig,
                        IRegistrationRepository IRegistrationRepository,
                        IPaymentRepository IPaymentRepository,
                        PaymentMapper paymentMapper) {
        this.vnPayConfig = vnPayConfig;
        this.IRegistrationRepository = IRegistrationRepository;
        this.IPaymentRepository = IPaymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Transactional
    public String createPaymentUrl(Registration registration, String ip) {
        try {
            // 1. Basic validation
            if (registration == null) {
                throw new IllegalArgumentException("Registration cannot be null");
            }
            // 2. Calculate amount before any other operation
            long amount = calculateAmount(registration);
            // 3. Create and set payment with LocalDateTime
            Payment payment = new Payment();
            payment.setAmount((int)(amount/100));
            payment.setPayDate(LocalDateTime.now());  // Fixed: Use LocalDateTime instead of Date
            registration.setPayment(payment);
            //   registration.setPaymentStatus(EPaymentStatus.UNPAID);
            Registration savedRegistration = IRegistrationRepository.save(registration);
            //   System.out.println("Saved registration ID: " + savedRegistration.getId());

            Map<String, String> vnp_Params = buildVNPayParams(savedRegistration, amount, ip);
            String paymentUrl = buildPaymentUrl(vnp_Params);
            //   System.out.println("Generated payment URL: " + paymentUrl);
            return paymentUrl;
        } catch (Exception e) {
            System.err.println("Error creating payment URL: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create payment URL: " + e.getMessage(), e);
        }
    }

    private long calculateAmount(Registration registration) {
        if (registration == null) {
            throw new IllegalArgumentException("Registration cannot be null");
        }
        Integer participants = registration.getNumberOfParticipants();
        if (participants == null || participants < 1) {
            throw new IllegalArgumentException("Number of participants must be greater than 0");
        }

        Double basePrice = registration.getService().getPrice();
        if (basePrice == null || basePrice <= 0) {
            throw new IllegalArgumentException("Service price must be greater than 0");
        }

        return Math.round(basePrice * participants * 100);
    }

    private Map<String, String> buildVNPayParams(Registration registration, long amount, String ip) {
        String createDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String txnRef = String.valueOf(registration.getId());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_BankCode", ""); // Optional
        vnp_Params.put("vnp_CreateDate", createDate);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_IpAddr", ip);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don xet nghiem:" + txnRef);
        vnp_Params.put("vnp_OrderType", "other"); // Changed from billpayment
        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        vnp_Params.put("vnp_TxnRef", txnRef);

        // Sort the parameters before signing
        return new TreeMap<>(vnp_Params);
    }

    private String buildPaymentUrl(Map<String, String> vnp_Params) {
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = vnPayConfig.getPayUrl() + "?" + query;
        String vnp_SecureHash = HmacUtils.hmacSha512Hex(vnPayConfig.getHashSecret(), hashData.toString());
        return queryUrl + "&vnp_SecureHash=" + vnp_SecureHash;
    }

    @Transactional
    public PaymentDTO validatePayment(Map<String, String> params) {
        try {
            String vnp_SecureHash = params.get("vnp_SecureHash");
            String vnp_TxnRef = params.get("vnp_TxnRef");

            if (vnp_SecureHash == null || vnp_TxnRef == null) {
                return null;
            }

            Registration registration = IRegistrationRepository.findById(Long.valueOf(vnp_TxnRef))
                    .orElse(null);
            if (registration == null) {
                return null;
            }

            boolean isValid = validatePaymentResponse(params);

            Payment payment = createPayment(params);
            Payment savedPayment = IPaymentRepository.save(payment);

            updateRegistrationStatus(registration, savedPayment, isValid);

            return paymentMapper.toDTO(savedPayment);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Payment createPayment(Map<String, String> params) {
        Payment payment = new Payment();
        payment.setOrderId(params.get("vnp_TxnRef"));
        payment.setAmount(Integer.parseInt(params.getOrDefault("vnp_Amount", "0")) / 100);
        payment.setTransactionNo(params.get("vnp_TransactionNo"));
        payment.setResponseCode(params.get("vnp_ResponseCode"));

        String payDateStr = params.get("vnp_PayDate");
        if (payDateStr != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            payment.setPayDate(LocalDateTime.parse(payDateStr, formatter));
        }

        return payment;
    }

    private boolean validatePaymentResponse(Map<String, String> params) {
        String vnp_SecureHash = params.get("vnp_SecureHash");
        String calculatedHash = calculateSecureHash(params);
        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        String vnp_TransactionStatus = params.get("vnp_TransactionStatus");

        return vnp_SecureHash.equals(calculatedHash) &&
                "00".equals(vnp_ResponseCode) &&
                "00".equals(vnp_TransactionStatus);
    }

    private String calculateSecureHash(Map<String, String> params) {
        Map<String, String> validParams = new TreeMap<>(params);
        validParams.remove("vnp_SecureHash");
        validParams.remove("vnp_SecureHashType");

        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : validParams.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                hashData.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                        .append("&");
            }
        }
        String hashDataStr = hashData.substring(0, hashData.length() - 1);
        return HmacUtils.hmacSha512Hex(vnPayConfig.getHashSecret(), hashDataStr);
    }
    private void updateRegistrationStatus(Registration registration, Payment payment, boolean isValid) {
        registration.setPayment(payment);
        registration.setPaymentStatus(isValid ? EPaymentStatus.PAID : EPaymentStatus.FAILED);
        IRegistrationRepository.save(registration);
    }
}