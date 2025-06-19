package com.genx.mapper;

import com.genx.dto.request.ParticipantRequest;
import com.genx.dto.response.ParticipantResponse;
import com.genx.entity.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    Participant toEntity(ParticipantRequest request);

    @Mapping(target = "kitEnteredByName", source = "kitEnteredBy.user.fullName")
    ParticipantResponse toResponse(Participant entity);
}
