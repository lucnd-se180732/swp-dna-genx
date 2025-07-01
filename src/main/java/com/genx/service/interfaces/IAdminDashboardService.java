package com.genx.service.interfaces;

import com.genx.dto.AdminDashboardDto;

public interface IAdminDashboardService {
    AdminDashboardDto getDashboardData();
    Long getMonthlyRevenue(int month, int year);
}
