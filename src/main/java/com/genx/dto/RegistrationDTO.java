package com.genx.dto;

import com.genx.enums.EPaymentStatus;
import com.genx.enums.ETypeOfService;

import java.time.LocalDateTime;
import java.util.List;

public class RegistrationDTO {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String email;
    private ETypeOfService ETypeOfService;
    private String appointmentDate;
    private Integer numberOfParticipants;
    private EPaymentStatus EPaymentStatus;
    private Long serviceId;  // Foreign key reference to Services table
    private List<ParticipantDTO> participants;
    private LocalDateTime createdAt;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ETypeOfService getTypeOfService() {
        return ETypeOfService;
    }

    public void setTypeOfService(ETypeOfService ETypeOfService) {
        this.ETypeOfService = ETypeOfService;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public EPaymentStatus getPaymentStatus() {
        return EPaymentStatus;
    }

    public void setPaymentStatus(EPaymentStatus EPaymentStatus) {
        this.EPaymentStatus = EPaymentStatus;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public List<ParticipantDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantDTO> participants) {
        this.participants = participants;
    }

    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(Integer numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}