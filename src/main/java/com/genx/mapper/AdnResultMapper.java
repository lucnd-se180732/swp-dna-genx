package com.genx.mapper;

import com.genx.dto.response.AdnResultResponse;
import com.genx.entity.AdnResult;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = ParticipantMapper.class)
public interface AdnResultMapper {

    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "code", ignore = true) // Bỏ tự động, tự map thủ công
    AdnResultResponse toDto(AdnResult entity);

    AdnResult toEntity(AdnResultResponse dto);

    @AfterMapping
    default void mapDetails(AdnResult entity,
                            @MappingTarget AdnResultResponse dto,
                            @Context ParticipantMapper participantMapper) {
        if (entity.getBooking() != null) {
            // Gán code từ booking
            dto.setCode(entity.getBooking().getCode());

            //  Gán participants
            if (entity.getBooking().getParticipants() != null) {
                dto.setParticipants(
                        entity.getBooking().getParticipants().stream()
                                .map(participantMapper::toResponse)
                                .toList()
                );
            }
        }
    }
}