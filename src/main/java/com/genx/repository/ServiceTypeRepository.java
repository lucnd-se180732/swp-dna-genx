package com.genx.repository;

import com.genx.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeRepository extends JpaRepository<Service, Long> {

}
