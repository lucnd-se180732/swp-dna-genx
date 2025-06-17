package com.genx.repository;

import com.genx.entity.Booking;
import com.genx.enums.EBookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IBookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
    SELECT b FROM Booking b
    WHERE (:status IS NULL OR b.status = :status)
      AND (:bookingId IS NULL OR b.id = :bookingId)
""")
    Page<Booking> searchByStatusAndBookingId(
            @Param("status") EBookingStatus status,
            @Param("bookingId") Long bookingId,
            Pageable pageable
    );


}
