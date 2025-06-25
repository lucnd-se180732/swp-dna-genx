package com.genx.service.impl;

import com.genx.dto.AdminDashboardDto;
import com.genx.enums.ERole;
import com.genx.repository.BlogRepository;
import com.genx.repository.ServiceRepository;
import com.genx.repository.UserRepository;
import com.genx.service.interfaces.IAdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements IAdminDashboardService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ServiceRepository serviceRepository;

    @Autowired
    private final BlogRepository blogRepository;

    @Override
    public AdminDashboardDto getDashboardData() {
        long totalUsers = userRepository.count();
        long totalStaff = userRepository.countByRoleIn(List.of(ERole.LAB_STAFF, ERole.RECORDER_STAFF));
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