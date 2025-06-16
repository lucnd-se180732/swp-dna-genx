package com.genx.mapper;
import com.genx.dto.ParticipantDTO;
import com.genx.entity.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "yearOfBirth", target = "yearOfBirth")
    @Mapping(source = "identityNumber", target = "identityNumber")
    @Mapping(source = "issueDate", target = "issueDate")
    @Mapping(source = "issuePlace", target = "issuePlace")
    @Mapping(source = "relationship", target = "relationship")
    ParticipantDTO toDTO(Participant participant);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "yearOfBirth", target = "yearOfBirth")
    @Mapping(source = "identityNumber", target = "identityNumber")
    @Mapping(source = "issueDate", target = "issueDate")
    @Mapping(source = "issuePlace", target = "issuePlace")
    @Mapping(source = "relationship", target = "relationship")
    Participant toEntity(ParticipantDTO participantDTO);
}