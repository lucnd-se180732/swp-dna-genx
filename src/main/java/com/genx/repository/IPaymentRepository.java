package com.genx.repository;

import com.genx.entity.Payment;
import com.genx.enums.EPaymentStatus;
import com.genx.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IPaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(String orderId);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.responseCode = '00'")
    Optional<Long> sumSuccessfulPaymentAmount();







}