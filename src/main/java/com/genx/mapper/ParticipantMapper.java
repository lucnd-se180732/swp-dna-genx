package com.genx.mapper;
import com.genx.dto.response.ParticipantResponse;
import com.genx.entity.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "yearOfBirth", target = "yearOfBirth")
    @Mapping(source = "identityNumber", target = "identityNumber")
    @Mapping(source = "issueDate", target = "issueDate")
    @Mapping(source = "issuePlace", target = "issuePlace")
    @Mapping(source = "relationship", target = "relationship")
    @Named("participantToDTO")
    ParticipantResponse toDTO(Participant participant);

    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "yearOfBirth", target = "yearOfBirth")
    @Mapping(source = "identityNumber", target = "identityNumber")
    @Mapping(source = "issueDate", target = "issueDate")
    @Mapping(source = "issuePlace", target = "issuePlace")
    @Mapping(source = "relationship", target = "relationship")
    Participant toEntity(ParticipantResponse participantResponse);



    @Mapping(target = "kitEnteredByName", source = "kitEnteredBy.user.fullName")
    @Named("participantToResponse")
    @Mapping(target = "sampleStatus", source = "sampleStatus")
    ParticipantResponse toResponse(Participant entity);
}