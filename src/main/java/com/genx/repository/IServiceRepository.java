package com.genx.repository;
import com.genx.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IServiceRepository extends JpaRepository<Services, Long> {
    Services findByName(String name);
}