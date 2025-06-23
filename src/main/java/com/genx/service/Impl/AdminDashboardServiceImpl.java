package com.genx.service.Impl;

import com.genx.dto.AdminDashboardDto;
import com.genx.enums.ERole;
import com.genx.repository.BlogRepository;
import com.genx.repository.ServiceRepository;
import com.genx.repository.UserRepository;
import com.genx.service.interfaces.IAdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements IAdminDashboardService {

    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final BlogRepository blogRepository;

    @Override
    public AdminDashboardDto getDashboardData() {
        long totalUsers = userRepository.count();
        long totalStaff = userRepository.countByRoleIn(List.of(ERole.LAB_STAFF, ERole.RECORD_STAFF));
        long totalCustomers = userRepository.countByRole(ERole.CUSTOMER);
        long totalServices = serviceRepository.countByEnabled(true);
        long totalBlogs = blogRepository.count();

        return new AdminDashboardDto(
                totalUsers,
                totalStaff,
                totalCustomers,
                totalServices,
                totalBlogs
        );
    }
}