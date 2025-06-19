package com.genx.repository;

import com.genx.entity.StaffInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStaffInfoRepository extends JpaRepository<StaffInfo, Long> {

    // Custom query methods can be defined here if needed
    // For example:
    // List<StaffInfo> findByDepartment(String department);

    // You can also use Spring Data JPA's derived query methods
    // or define custom queries using @Query annotation if necessary
}
