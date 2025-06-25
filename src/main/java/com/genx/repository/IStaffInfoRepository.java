package com.genx.repository;

import com.genx.entity.StaffInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStaffInfoRepository extends JpaRepository<StaffInfo, Long> {

    StaffInfo findByUserId(Long userId);

}
