package com.genx.mapper;

import com.genx.dto.response.AdnResultResponse;
import com.genx.entity.AdnResult;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = ParticipantMapper.class)
public interface AdnResultMapper {

    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "code", ignore = true)
    AdnResultResponse toDto(AdnResult entity);

    AdnResult toEntity(AdnResultResponse dto);

    @AfterMapping
    default void mapDetails(AdnResult entity,
                            @MappingTarget AdnResultResponse dto,
                            @Context ParticipantMapper participantMapper) {
        if (entity.getBooking() != null) {
            dto.setCode(entity.getBooking().getCode());
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