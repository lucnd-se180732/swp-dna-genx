package com.genx.mapper;

import com.genx.dto.request.BookingRequest;
import com.genx.dto.response.BookingResponse;
import com.genx.entity.Booking;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ParticipantMapper.class})
public interface BookingMapper {
    @Mapping(source = "service.id", target = "serviceId")
    @Mapping(source = "collectionMethod", target = "collectionMethod")
    @Mapping(source = "paymentStatus", target = "paymentStatus")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "appointmentDate", target = "appointmentDate")
    @Mapping(source = "participants", target = "participants")
    @Mapping(source = "numberOfParticipants", target = "numberOfParticipants")
    @Mapping(source = "booking.createdAt", target = "createdAt")
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

}