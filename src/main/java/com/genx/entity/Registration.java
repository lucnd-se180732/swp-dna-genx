package com.genx.entity;

import com.genx.enums.PaymentStatus;
import com.genx.enums.TypeOfService;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "registrations")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "appointment_date")
    private String appointmentDate;

    @Column(name = "number_of_participants")
    private Integer numberOfParticipants;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services service;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_of_service")
    private TypeOfService typeOfService;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToMany(mappedBy = "registration", cascade = CascadeType.ALL)
    private List<Participant> participants = new ArrayList<>();

    // Default constructor
    public Registration() {
    }

    // Constructor with fields
    public Registration(Long id, String fullName, String phoneNumber, String address,
                        String email, String appointmentDate, Services service,
                        TypeOfService typeOfService, PaymentStatus paymentStatus,
                        Payment payment, Integer numberOfParticipants) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.appointmentDate = appointmentDate;
        this.service = service;
        this.typeOfService = typeOfService;
        this.paymentStatus = paymentStatus;
        this.payment = payment;
        this.numberOfParticipants = numberOfParticipants;
    }



    // Getters and setters
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

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Services getService() {
        return service;
    }

    public void setService(Services service) {
        this.service = service;
    }

    public TypeOfService getTypeOfService() {
        return typeOfService;
    }

    public void setTypeOfService(TypeOfService typeOfService) {
        this.typeOfService = typeOfService;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    // Add getter and setter for the new field
    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(Integer numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    // Modify addParticipant method to update the count
    public void addParticipant(Participant participant) {
        if (participants == null) {
            participants = new ArrayList<>();
        }
        participants.add(participant);
        participant.setRegistration(this);
        this.numberOfParticipants = participants.size();
    }

    // Modify removeParticipant method to update the count
    public void removeParticipant(Participant participant) {
        participants.remove(participant);
        participant.setRegistration(null);
        this.numberOfParticipants = participants.size();
    }
}