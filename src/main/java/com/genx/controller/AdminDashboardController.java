package com.genx.controller;

import com.genx.dto.AdminDashboardDto;
import com.genx.enums.EPaymentStatus;
import com.genx.repository.IBookingRepository;
import com.genx.service.interfaces.IAdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final IAdminDashboardService dashboardService;


    @GetMapping
    public ResponseEntity<AdminDashboardDto> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }


    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Long>> getMonthlyRevenue(
            @RequestParam int month,
            @RequestParam int year
    ) {
        Long revenue = dashboardService.getMonthlyRevenue(month, year);
        return ResponseEntity.ok(Map.of("monthlyRevenue", revenue));
    }
}