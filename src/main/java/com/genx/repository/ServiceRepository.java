package com.genx.repository;

import com.genx.entity.Service;
import com.genx.enums.ECaseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByCaseType(ECaseType caseType);
    List<Service> findByEnabled(boolean enabled);

    long countByEnabled(boolean enabled);
}
