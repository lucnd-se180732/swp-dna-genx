package com.genx.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminDashboardDto {

    private long totalUsers;
    private long totalStaff;
    private long totalCustomers;
    private long totalServices;
    private long totalBlogs;

    // private long totalBookingsToday;
    // private BigDecimal totalRevenueToday;
}
