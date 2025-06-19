package com.genx.mapper;

import com.genx.dto.request.BookingRequest;
import com.genx.dto.response.BookingResponse;
import com.genx.dto.response.BookingSummaryResponse;
import com.genx.entity.Booking;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = ParticipantMapper.class)
public interface BookingMapper {
    @Mapping(source = "service.id", target = "serviceId")
    @Mapping(source = "collectionMethod", target = "collectionMethod")
    @Mapping(source = "paymentStatus", target = "paymentStatus")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "appointmentDate", target = "appointmentDate")
    //@Mapping(source = "participants", target = "participants")
    @Mapping(source = "participants", target = "participants", qualifiedByName = "participantToResponse")
    @Mapping(source = "numberOfParticipants", target = "numberOfParticipants")
    @Mapping(source = "booking.createdAt", target = "createdAt")
    @Named("toDTO")
    BookingResponse toDTO(Booking booking);

    @Mapping(source = "serviceId", target = "service.id")
    @Mapping(source = "collectionMethod", target = "collectionMethod")
    @Mapping(source = "paymentStatus", target = "paymentStatus")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "appointmentDate", target = "appointmentDate")
    @Mapping(source = "participants", target = "participants")
    @Mapping(source = "numberOfParticipants", target = "numberOfParticipants")
    Booking toEntity(BookingRequest bookingRequest);

    // for boooking details
    @Mapping(target = "id", source = "id")
    @Mapping(target = "customerName", source = "customer.user.fullName")
    @Mapping(target = "recordStaffName", source = "recordStaff.user.fullName")
    @Mapping(target = "serviceId", source = "service.id")
    //@Mapping(target = "serviceName", source = "service.name")
    @Mapping(target = "servicePrice", source = "service.price")
    @Mapping(target = "collectionMethod", source = "collectionMethod")
    @Mapping(source = "participants", target = "participants", qualifiedByName = "participantToResponse")
    @Named("toResponse")
    BookingResponse toResponse(Booking booking);

    // for booking summary
    @Mapping(target = "id", source = "id")
    BookingSummaryResponse toSummary(Booking booking);
}