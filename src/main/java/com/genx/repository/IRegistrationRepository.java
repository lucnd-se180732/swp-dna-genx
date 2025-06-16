package com.genx.repository;

import com.genx.entity.Registration;
import com.genx.enums.EPaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByEPaymentStatus(EPaymentStatus status);
}