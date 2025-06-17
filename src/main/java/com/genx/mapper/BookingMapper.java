package com.genx.mapper;

import com.genx.dto.response.BookingResponse;
import com.genx.dto.response.BookingSummaryResponse;
import com.genx.entity.Booking;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = ParticipantMapper.class)
public interface BookingMapper {


    // for boooking details
    @Mapping(target = "bookingId", source = "id")
    @Mapping(target = "recordStaffName", source = "recordStaff.user.fullName")
    @Mapping(target = "serviceId", source = "service.id")
    @Mapping(target = "serviceName", source = "service.name")
    @Mapping(target = "servicePrice", source = "service.price")
//    @Mapping(target = "collectionOptionId", source = "collectionOption.id")
//    @Mapping(target = "collectionOptionName", source = "collectionOption.name")
    BookingResponse toResponse(Booking booking);

    // for booking summary
    @Mapping(target = "bookingId", source = "id")
    BookingSummaryResponse toSummary(Booking booking);
}
