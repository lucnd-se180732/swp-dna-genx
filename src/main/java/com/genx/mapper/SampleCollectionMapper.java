package com.genx.mapper;

import com.genx.dto.response.SampleCollectionResponse;
import com.genx.entity.SampleCollection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SampleCollectionMapper {

    @Mapping(target = "collectionId", source = "id")
    @Mapping(target = "bookingRegistrantName", source = "booking.customer.user.fullName")
    @Mapping(target = "collectedByName", source = "collectedBy.fullName")
    SampleCollectionResponse toResponse(SampleCollection entity);
}
