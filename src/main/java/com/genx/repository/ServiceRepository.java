package com.genx.repository;

import com.genx.entity.Service;
import com.genx.enums.ECaseType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByCaseType(ECaseType caseType);
    List<Service> findByEnabled(boolean enabled);
}
