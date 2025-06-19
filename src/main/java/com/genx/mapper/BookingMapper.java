package com.genx.mapper;

import com.genx.dto.response.BookingResponse;
import com.genx.dto.response.BookingSummaryResponse;
import com.genx.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ParticipantMapper.class})
public interface BookingMapper {

    // Ánh xạ chi tiết đơn đặt (BookingResponse)
    @Mapping(target = "bookingId", source = "id")
    @Mapping(target = "recordStaffName", source = "recordStaff.user.fullName")
    @Mapping(target = "serviceId", source = "service.id")
    @Mapping(target = "serviceName", source = "service.name")
    @Mapping(target = "servicePrice", source = "service.price")
    @Mapping(target = "collectionMethod", source = "collectionMethod")
    @Mapping(target = "participants", source = "participants") // nếu có danh sách participants
    BookingResponse toResponse(Booking booking);

    // Ánh xạ đơn giản cho danh sách tóm tắt
    @Mapping(target = "bookingId", source = "id")
    @Mapping(target = "status", source = "status")
//    @Mapping(target = "serviceName", source = "service.name")
    @Mapping(target = "recordStaffName", source = "recordStaff.user.fullName")
    BookingSummaryResponse toSummary(Booking booking);
}
