package com.genx.repository;

import com.genx.entity.SampleCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ISampleCollectionRepository extends JpaRepository<SampleCollection, Long> {
    Optional<SampleCollection> findByBooking_Id(Long bookingId);
}
