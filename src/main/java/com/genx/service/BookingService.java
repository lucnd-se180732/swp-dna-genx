//package com.genx.service;
//
//import com.genx.dto.request.BookingRequest;
//import com.genx.dto.response.BookingResponse;
//import com.genx.entity.*;
//import com.genx.enums.EBookingStatus;
//import com.genx.enums.EPaymentStatus;
//import com.genx.enums.ESampleCollectionStatus;
//import com.genx.mapper.BookingMapper;
//import com.genx.repository.IBookingRepository;
//import com.genx.repository.ICustomerRepository;
//import com.genx.repository.IPaymentRepository;
//import com.genx.repository.IServiceRepository;
//import com.genx.security.SecurityUtil;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataIntegrityViolationException;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@org.springframework.stereotype.Service
//public class BookingService {
//
//    @Autowired
//    private IBookingRepository bookingRepository;
//
//    @Autowired
//    private IServiceRepository serviceRepository;
//
//    @Autowired
//    private ICustomerRepository customerRepository;
//
//    @Autowired
//    private VNPayService vnPayService;
//
//    @Autowired
//    private IPaymentRepository paymentRepository;
//
//    @Autowired
//    private BookingMapper bookingMapper;
//
////    @Transactional
////    public BookingResponse createRegistration(BookingRequest bookingRequest) {
////        try {
////            Integer numParticipants = bookingRequest.getNumberOfParticipants();
////            if (numParticipants == null || numParticipants < 1) {
////                throw new IllegalArgumentException("Number of participants must be greater than 0");
////            }
////
////            Service service = serviceRepository.findById(bookingRequest.getServiceId())
////                    .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + bookingRequest.getServiceId()));
////
////            Booking booking = bookingMapper.toEntity(bookingRequest);
////            booking.setService(service);
////            booking.setPaymentStatus(EPaymentStatus.UNPAID);
////            booking.setStatus(EBookingStatus.PENDING);
////
////            User user = SecurityUtil.getCurrentUser()
////                    .orElseThrow(() -> new RuntimeException("Không có người dùng đăng nhập"));
////            Customer customer = customerRepository.findById(user.getId())
////                    .orElseThrow(() -> new RuntimeException("Không tìm thấy customer từ user"));
////            booking.setCustomer(customer);
////
////            // Gán booking cho các participants
////            if (booking.getParticipants() != null) {
////                booking.getParticipants().forEach(p -> p.setBooking(booking));
////            }
////
////            String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
////            String code;
////            do {
////                String randomPart = String.format("%06d", (int)(Math.random() * 1_000_000));
////                code = "BK" + datePart + "_" + randomPart;
////            } while (bookingRepository.existsByCode(code));
////
////            booking.setCode(code);
////            // B1: Lưu booking trước để có ID
////            Booking savedBooking = bookingRepository.save(booking);
////
////            // B2: Tạo và lưu payment riêng
////            Payment payment = new Payment();
////            payment.setAmount(service.getPrice().intValue() * savedBooking.getNumberOfParticipants());
////            payment.setBooking(savedBooking); // Gán booking đã có ID
////            Payment savedPayment = paymentRepository.save(payment);
////
////            // B3: Gán lại payment vào booking nếu bạn dùng quan hệ 2 chiều
////            savedBooking.setPayment(savedPayment);
////            bookingRepository.save(savedBooking); // cập nhật lại booking
////
////            return bookingMapper.toDTO(savedBooking);
////        } catch (Exception e) {
////            throw new RuntimeException("Failed to create registration: " + e.getMessage(), e);
////        }
////    }
//
////    @Transactional
////    public BookingResponse updatePaymentStatus(String orderId, EPaymentStatus status) {
////        Booking booking = bookingRepository.findByPaymentOrderId(orderId)
////                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + orderId));
////
////
////        booking.setPaymentStatus(status);
////        Booking updatedBooking = bookingRepository.save(booking);
////        return bookingMapper.toDTO(updatedBooking);
////    }
//
////    public BookingResponse findById(Long id) {
////        return bookingRepository.findById(id)
////                .map(bookingMapper::toDTO)
////                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
////    }
////
////    @Transactional
////    public void deleteRegistration(Long id) {
////        if (!bookingRepository.existsById(id)) {
////            throw new EntityNotFoundException("Booking not found with id: " + id);
////        }
////        bookingRepository.deleteById(id);
////    }
//
////    @Transactional
////    public BookingResponse updateRegistration(Long id, BookingRequest bookingRequest) {
////        Booking existingBooking = bookingRepository.findById(id)
////                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
////
////        if (existingBooking.getPaymentStatus() == EPaymentStatus.PAID) {
////            throw new IllegalStateException("Cannot update paid booking");
////        }
////
////        // Fetch the service first
////        Service service = serviceRepository.findById(bookingRequest.getServiceId())
////                .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + bookingRequest.getServiceId()));
////
////        Booking booking = bookingMapper.toEntity(bookingRequest);
////        booking.setId(id);
////        booking.setService(service);
////        booking.setPayment(existingBooking.getPayment());
////        booking.setPaymentStatus(existingBooking.getPaymentStatus());
////        Booking updatedBooking = bookingRepository.save(booking);
////        return bookingMapper.toDTO(updatedBooking);
////    }
//
//    // Trả về registration đã có service, payment, ... đầy đủ
////    public Booking getFullRegistrationById(Long id) {
////        return bookingRepository.findById(id)
////                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
////    }
////
////    public BookingResponse getRegistrationById(Long id) {
////        Booking booking = bookingRepository.findById(id)
////                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
////        return bookingMapper.toDTO(booking); // đây là lúc dùng mapper đúng nghĩa
////    }
////
////    public BookingResponse cancelRegistration(Long id) {
////        Booking booking = bookingRepository.findById(id)
////                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
////
////        // Both field and setter name match exactly with entity
////        booking.setPaymentStatus(EPaymentStatus.CANCELLED);
////
////        try {
////            Booking saved = bookingRepository.save(booking);
////            return bookingMapper.toDTO(saved);
////        } catch (DataIntegrityViolationException e) {
////            throw new IllegalStateException("Cannot cancel this booking. Invalid status transition.");
////        }
////    }
////
////    public List<BookingResponse> getRegistrationsByStatus(EPaymentStatus status) {
////        Long customerId = SecurityUtil.getCurrentUserId()
////                .orElseThrow(() -> new RuntimeException("Không tìm thấy user đang đăng nhập"));
////
////        return bookingRepository.findByCustomerIdAndPaymentStatus(customerId, status).stream()
////                .map(bookingMapper::toDTO)
////                .collect(Collectors.toList());
////    }
////
////    public List<BookingResponse> getAllRegistrations() {
////        Long customerId = SecurityUtil.getCurrentUserId()
////                .orElseThrow(() -> new RuntimeException("Không tìm thấy user đang đăng nhập"));
////
////        return bookingRepository.findByCustomerId(customerId)
////                .stream()
////                .map(bookingMapper::toDTO)
////                .collect(Collectors.toList());
////    }
////
////    public Optional<Booking> getBookingByPayment(Payment payment) {
////        return bookingRepository.findByPayment(payment);
////    }
////
////    public List<BookingResponse> getAllApplicationsSentToLab() {
////        // Không cần lấy customerId nữa
////        return bookingRepository.findBySampleCollectionStatus(ESampleCollectionStatus.SENT_TO_LAB)
////                .stream()
////                .map(bookingMapper::toDTO)
////                .collect(Collectors.toList());
////    }
//
//}