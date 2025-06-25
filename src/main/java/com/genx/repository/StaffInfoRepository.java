package com.genx.repository;

import com.genx.entity.StaffInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffInfoRepository extends JpaRepository<StaffInfo, Long> {
}