package com.genx.service;

import com.genx.dto.request.BookingRequest;
import com.genx.dto.response.BookingResponse;
import com.genx.entity.Booking;
import com.genx.entity.Payment;
import com.genx.entity.Service;
import com.genx.enums.EPaymentStatus;
import com.genx.mapper.BookingMapper;
import com.genx.repository.IBookingRepository;
import com.genx.repository.IServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class BookingService {

    @Autowired
    private IBookingRepository IBookingRepository;

    @Autowired
    private IServiceRepository IServiceRepository;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private BookingMapper bookingMapper;

    @Transactional
    public BookingResponse createRegistration(BookingRequest bookingRequest) {
        try {


            // Validate number of participants
            Integer numParticipants = bookingRequest.getNumberOfParticipants();
            if (numParticipants == null || numParticipants < 1) {
                throw new IllegalArgumentException("Number of participants must be greater than 0");
            }

            Service service = IServiceRepository.findById(bookingRequest.getServiceId())
                    .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + bookingRequest.getServiceId()));

            Booking booking = bookingMapper.toEntity(bookingRequest);
            booking.setService(service);
            booking.setPaymentStatus(EPaymentStatus.UNPAID);
            // Add price validation
            System.out.println("Service ID: " + service.getId());
            System.out.println("Service price: " + service.getPrice());
            // Ensure number of participants is set
            if (booking.getNumberOfParticipants() == null || booking.getNumberOfParticipants() < 1) {
                booking.setNumberOfParticipants(numParticipants); // This should now work with int
            }

            // Set participants relationships if they exist
            if (booking.getParticipants() != null) {
                booking.getParticipants().forEach(participant -> participant.setBooking(booking));
            }

            // Calculate total amount based on numberOfParticipants
            double totalAmount = service.getPrice() * booking.getNumberOfParticipants();
            Payment payment = new Payment();
            payment.setAmount((int) totalAmount);
            booking.setPayment(payment);

            Booking savedBooking = IBookingRepository.save(booking);
            return bookingMapper.toDTO(savedBooking);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create registration: " + e.getMessage(), e);
        }
    }
    @Transactional
    public BookingResponse updatePaymentStatus(String orderId, EPaymentStatus status) {
        Booking booking = IBookingRepository.findById(Long.valueOf(orderId))
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + orderId));

        booking.setPaymentStatus(status);
        Booking updatedBooking = IBookingRepository.save(booking);
        return bookingMapper.toDTO(updatedBooking);
    }

    public BookingResponse findById(Long id) {
        return IBookingRepository.findById(id)
                .map(bookingMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
    }

    @Transactional
    public void deleteRegistration(Long id) {
        if (!IBookingRepository.existsById(id)) {
            throw new EntityNotFoundException("Booking not found with id: " + id);
        }
        IBookingRepository.deleteById(id);
    }
    @Transactional
    public BookingResponse updateRegistration(Long id, BookingRequest bookingRequest) {
        Booking existingBooking = IBookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));

        if (existingBooking.getPaymentStatus() == EPaymentStatus.PAID) {
            throw new IllegalStateException("Cannot update paid booking");
        }

        // Fetch the service first
        Service service = IServiceRepository.findById(bookingRequest.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + bookingRequest.getServiceId()));

        Booking booking = bookingMapper.toEntity(bookingRequest);
        booking.setId(id);
        booking.setService(service);
        booking.setPayment(existingBooking.getPayment());
        booking.setPaymentStatus(existingBooking.getPaymentStatus());
        Booking updatedBooking = IBookingRepository.save(booking);
        return bookingMapper.toDTO(updatedBooking);
    }
    // Trả về registration đã có service, payment, ... đầy đủ
    public Booking getFullRegistrationById(Long id) {
        return IBookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
    }

    public BookingResponse getRegistrationById(Long id) {
        Booking booking = IBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        return bookingMapper.toDTO(booking); // đây là lúc dùng mapper đúng nghĩa
    }
    public BookingResponse cancelRegistration(Long id) {
        Booking booking = IBookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        // Both field and setter name match exactly with entity
        booking.setPaymentStatus(EPaymentStatus.CANCELLED);

        try {
            Booking saved = IBookingRepository.save(booking);
            return bookingMapper.toDTO(saved);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Cannot cancel this booking. Invalid status transition.");
        }
    }

    public List<BookingResponse> getRegistrationsByStatus(EPaymentStatus status) {
        return IBookingRepository.findByPaymentStatus(status).stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getAllRegistrations() {
        return IBookingRepository.findAll()
                .stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());
    }
}