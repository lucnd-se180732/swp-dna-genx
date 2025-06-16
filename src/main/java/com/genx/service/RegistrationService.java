package com.genx.service;

import com.genx.dto.RegistrationDTO;
import com.genx.entity.Payment;
import com.genx.entity.Registration;
import com.genx.entity.Services;
import com.genx.enums.EPaymentStatus;
import com.genx.mapper.RegistrationMapper;
import com.genx.repository.IRegistrationRepository;
import com.genx.repository.IServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    @Autowired
    private IRegistrationRepository IRegistrationRepository;

    @Autowired
    private IServiceRepository IServiceRepository;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private RegistrationMapper registrationMapper;

    @Transactional
    public RegistrationDTO createRegistration(RegistrationDTO registrationDTO) {
        try {


            // Validate number of participants
            Integer numParticipants = registrationDTO.getNumberOfParticipants();
            if (numParticipants == null || numParticipants < 1) {
                throw new IllegalArgumentException("Number of participants must be greater than 0");
            }

            Services service = IServiceRepository.findById(registrationDTO.getServiceId())
                    .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + registrationDTO.getServiceId()));

            Registration registration = registrationMapper.toEntity(registrationDTO);
            registration.setService(service);
            registration.setPaymentStatus(EPaymentStatus.UNPAID);
            // Add price validation
            System.out.println("Service ID: " + service.getId());
            System.out.println("Service price: " + service.getPrice());
            // Ensure number of participants is set
            if (registration.getNumberOfParticipants() == null || registration.getNumberOfParticipants() < 1) {
                registration.setNumberOfParticipants(numParticipants); // This should now work with int
            }

            // Set participants relationships if they exist
            if (registration.getParticipants() != null) {
                registration.getParticipants().forEach(participant -> participant.setRegistration(registration));
            }

            // Calculate total amount based on numberOfParticipants
            double totalAmount = service.getPrice() * registration.getNumberOfParticipants();
            Payment payment = new Payment();
            payment.setAmount((int) totalAmount);
            registration.setPayment(payment);

            Registration savedRegistration = IRegistrationRepository.save(registration);
            return registrationMapper.toDTO(savedRegistration);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create registration: " + e.getMessage(), e);
        }
    }
    @Transactional
    public RegistrationDTO updatePaymentStatus(String orderId, EPaymentStatus status) {
        Registration registration = IRegistrationRepository.findById(Long.valueOf(orderId))
                .orElseThrow(() -> new EntityNotFoundException("Registration not found with id: " + orderId));

        registration.setPaymentStatus(status);
        Registration updatedRegistration = IRegistrationRepository.save(registration);
        return registrationMapper.toDTO(updatedRegistration);
    }

    public RegistrationDTO findById(Long id) {
        return IRegistrationRepository.findById(id)
                .map(registrationMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Registration not found with id: " + id));
    }

    @Transactional
    public void deleteRegistration(Long id) {
        if (!IRegistrationRepository.existsById(id)) {
            throw new EntityNotFoundException("Registration not found with id: " + id);
        }
        IRegistrationRepository.deleteById(id);
    }
    @Transactional
    public RegistrationDTO updateRegistration(Long id, RegistrationDTO registrationDTO) {
        Registration existingRegistration = IRegistrationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registration not found with id: " + id));

        if (existingRegistration.getPaymentStatus() == EPaymentStatus.PAID) {
            throw new IllegalStateException("Cannot update paid registration");
        }

        // Fetch the service first
        Services service = IServiceRepository.findById(registrationDTO.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + registrationDTO.getServiceId()));

        Registration registration = registrationMapper.toEntity(registrationDTO);
        registration.setId(id);
        registration.setService(service);
        registration.setPayment(existingRegistration.getPayment());
        registration.setPaymentStatus(existingRegistration.getPaymentStatus());
        Registration updatedRegistration = IRegistrationRepository.save(registration);
        return registrationMapper.toDTO(updatedRegistration);
    }
    // Trả về registration đã có service, payment, ... đầy đủ
    public Registration getFullRegistrationById(Long id) {
        return IRegistrationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registration not found with id: " + id));
    }

    public RegistrationDTO getRegistrationById(Long id) {
        Registration registration = IRegistrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found with id: " + id));
        return registrationMapper.toDTO(registration); // đây là lúc dùng mapper đúng nghĩa
    }
    public RegistrationDTO cancelRegistration(Long id) {
        Registration registration = IRegistrationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registration not found"));

        // Both field and setter name match exactly with entity
        registration.setEPaymentStatus(EPaymentStatus.CANCELLED);

        try {
            Registration saved = IRegistrationRepository.save(registration);
            return registrationMapper.toDTO(saved);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Cannot cancel this registration. Invalid status transition.");
        }
    }

    public List<RegistrationDTO> getRegistrationsByStatus(EPaymentStatus status) {
        return IRegistrationRepository.findByEPaymentStatus(status).stream()
                .map(registrationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<RegistrationDTO> getAllRegistrations() {
        return IRegistrationRepository.findAll()
                .stream()
                .map(registrationMapper::toDTO)
                .collect(Collectors.toList());
    }
}