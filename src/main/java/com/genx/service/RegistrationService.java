package com.genx.service;


import com.genx.dto.RegistrationDTO;
import com.genx.entity.Payment;
import com.genx.entity.Registration;
import com.genx.entity.Services;
import com.genx.enums.PaymentStatus;
import com.genx.mapper.RegistrationMapper;
import com.genx.repository.RegistrationRepository;
import com.genx.repository.ServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private RegistrationMapper registrationMapper;

    @Transactional
    public RegistrationDTO createRegistration(RegistrationDTO registrationDTO) {
        try {
            // Debug log
            System.out.println("DTO numberOfParticipants: " + registrationDTO.getNumberOfParticipants());

            // Validate number of participants
            Integer numParticipants = registrationDTO.getNumberOfParticipants();
            if (numParticipants == null || numParticipants < 1) {
                throw new IllegalArgumentException("Number of participants must be greater than 0");
            }

            Services service = serviceRepository.findById(registrationDTO.getServiceId())
                    .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + registrationDTO.getServiceId()));

            Registration registration = registrationMapper.toEntity(registrationDTO);
            registration.setService(service);
            registration.setPaymentStatus(PaymentStatus.UNPAID);
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

            Registration savedRegistration = registrationRepository.save(registration);
            return registrationMapper.toDTO(savedRegistration);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create registration: " + e.getMessage(), e);
        }
    }
    @Transactional
    public RegistrationDTO updatePaymentStatus(String orderId, PaymentStatus status) {
        Registration registration = registrationRepository.findById(Long.valueOf(orderId))
                .orElseThrow(() -> new EntityNotFoundException("Registration not found with id: " + orderId));

        registration.setPaymentStatus(status);
        Registration updatedRegistration = registrationRepository.save(registration);
        return registrationMapper.toDTO(updatedRegistration);
    }

    public RegistrationDTO findById(Long id) {
        return registrationRepository.findById(id)
                .map(registrationMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Registration not found with id: " + id));
    }

    @Transactional
    public void deleteRegistration(Long id) {
        if (!registrationRepository.existsById(id)) {
            throw new EntityNotFoundException("Registration not found with id: " + id);
        }
        registrationRepository.deleteById(id);
    }
    @Transactional
    public RegistrationDTO updateRegistration(Long id, RegistrationDTO registrationDTO) {
        Registration existingRegistration = registrationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registration not found with id: " + id));

        if (existingRegistration.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException("Cannot update paid registration");
        }

        // Fetch the service first
        Services service = serviceRepository.findById(registrationDTO.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + registrationDTO.getServiceId()));

        Registration registration = registrationMapper.toEntity(registrationDTO);
        registration.setId(id);
        registration.setService(service);
        registration.setPayment(existingRegistration.getPayment());
        registration.setPaymentStatus(existingRegistration.getPaymentStatus());
        Registration updatedRegistration = registrationRepository.save(registration);
        return registrationMapper.toDTO(updatedRegistration);
    }
    // Trả về registration đã có service, payment, ... đầy đủ
    public Registration getFullRegistrationById(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registration not found with id: " + id));
    }

    // Trả về service theo ID
    public Services getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + id));
    }
}