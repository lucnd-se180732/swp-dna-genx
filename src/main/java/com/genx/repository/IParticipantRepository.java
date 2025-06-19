package com.genx.repository;

import com.genx.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByBooking_Id(Long bookingId);
}
