package com.genx.repository;
import com.genx.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    // Custom query methods can be defined here if needed
    // For example:
    // List<User> findByStatus(Status status);
    // List<User> findByStaffType(StaffType staffType);

}
