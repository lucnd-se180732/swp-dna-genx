package com.genx.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_id")
    private String orderId;
    @Column(name = "amount")
    private int amount;
    @Column(name = "transaction_no")
    private String transactionNo;
    @Column(name = "response_code")
    private String responseCode;
    @Column(name = "pay_date")
    private LocalDateTime payDate;

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public String getTransactionNo() { return transactionNo; }
    public void setTransactionNo(String transactionNo) { this.transactionNo = transactionNo; }

    public String getResponseCode() { return responseCode; }
    public void setResponseCode(String responseCode) { this.responseCode = responseCode; }

    public LocalDateTime getPayDate() { return payDate; }
    public void setPayDate(LocalDateTime payDate) { this.payDate = payDate; }
}
