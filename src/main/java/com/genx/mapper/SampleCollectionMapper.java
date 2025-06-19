package com.genx.mapper;

import com.genx.dto.response.SampleCollectionResponse;
import com.genx.entity.SampleCollection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface SampleCollectionMapper {
    @Mappings({
            @Mapping(target = "collectionId", source = "id"),
            @Mapping(target = "bookingId", source = "booking.id"),
            @Mapping(target = "bookingRegistrantName", source = "booking.customer.user.fullName"),
            @Mapping(target = "collectedByName", expression = "java(getFullName(entity))"),
            @Mapping(target = "status", source = "status")
    })
    SampleCollectionResponse toResponse(SampleCollection entity);

    // Tự xử lý collectedByName
    default String getFullName(SampleCollection entity) {
        if (entity.getCollectedBy() != null && entity.getCollectedBy().getUser() != null) {
            return entity.getCollectedBy().getUser().getFullName();
        }
        return null;
    }
}
