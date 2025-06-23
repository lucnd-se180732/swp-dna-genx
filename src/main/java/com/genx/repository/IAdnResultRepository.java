package com.genx.repository;

import com.genx.entity.AdnResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;

public interface IAdnResultRepository extends JpaRepository<AdnResult, Long> {

    Optional<AdnResult> findByBooking_Id(Long bookingId);
}
