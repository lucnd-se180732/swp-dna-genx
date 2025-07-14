package com.genx.service.impl;

import com.genx.dto.AdminDashboardDto;
import com.genx.enums.ERole;
import com.genx.enums.EPaymentStatus;
import com.genx.mapper.BookingMapper;
import com.genx.repository.*;
import com.genx.service.interfaces.IAdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements IAdminDashboardService {

    @Autowired
    private final IUserRepository userRepository;

    @Autowired
    private final IServiceRepository serviceRepository;

    @Autowired
    private final BlogRepository blogRepository;

    @Autowired
    private final IPaymentRepository paymentRepository;

    @Autowired
    private final IBookingRepository bookingRepository;

    @Autowired
    private final BookingMapper bookingMapper;

    @Override
    public AdminDashboardDto getDashboardData() {
        long totalUsers = userRepository.count();
        long totalStaff = userRepository.countByRoleIn(List.of(ERole.LAB_STAFF, ERole.RECORDER_STAFF));
        long totalCustomers = userRepository.countByRole(ERole.CUSTOMER);
        long totalServices = serviceRepository.countByEnabled(true);
        long totalBlogs = blogRepository.count();
        long totalPayments = bookingRepository.countByPaymentStatus(EPaymentStatus.PAID);
        long totalRevenue = paymentRepository.sumSuccessfulPaymentAmount().orElse(0L);

        long todayRevenue = bookingRepository
                .sumTodayRevenue(EPaymentStatus.PAID)
                .orElse(0L);

        LocalDate now = LocalDate.now();
        long monthlyRevenue = bookingRepository
                .sumMonthlyRevenue(EPaymentStatus.PAID, now.getMonthValue(), now.getYear())
                .orElse(0L);

        return new AdminDashboardDto(
                totalUsers,
                totalStaff,
                totalCustomers,
                totalServices,
                totalBlogs,
                totalPayments,
                totalRevenue,
                todayRevenue,
                monthlyRevenue
        );
    }

    @Override
    public Long getMonthlyRevenue(int month, int year) {
        return bookingRepository.sumMonthlyRevenue(EPaymentStatus.PAID, month, year)
                .orElse(0L);
    }
}