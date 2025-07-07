package com.genx.repository;

import com.genx.entity.KitStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IKitStockRepository extends JpaRepository<KitStock, Long> {
}
