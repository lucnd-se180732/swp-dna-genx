package com.genx.entity;

import com.genx.enums.EBookingStatus;
import com.genx.enums.ECollectionMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
public class Booking extends BaseEntity{


    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "identity_number", nullable = true, length = 100)
    private String identityNumber;

    @Column(name = "number_of_participants", nullable = false)
    private Integer numberOfParticipants;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "record_staff_id")
    private StaffInfo recordStaff; // hoặc RecordStaff nếu bạn có entity riêng

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "option_collect")
    private ECollectionMethod collectionOption;

    // Giả sử bạn chưa cần xử lý payment nên bỏ payment_id ở đây
    // Nếu cần thì thêm: private Payment payment;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EBookingStatus status = EBookingStatus.CONFIRMED;

    @Column(name = "note", length = 500)
    private String note;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();
}
