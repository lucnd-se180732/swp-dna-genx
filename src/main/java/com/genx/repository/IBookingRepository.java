package com.genx.repository;

import com.genx.entity.Booking;
import com.genx.enums.EPaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByPaymentStatus(EPaymentStatus status);
}