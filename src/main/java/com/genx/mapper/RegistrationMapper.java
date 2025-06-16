package com.genx.mapper;

import com.genx.dto.RegistrationDTO;
import com.genx.entity.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ParticipantMapper.class})
public interface RegistrationMapper {
    @Mapping(source = "service.id", target = "serviceId")
    @Mapping(source = "typeOfService", target = "typeOfService")
    @Mapping(source = "paymentStatus", target = "paymentStatus")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "appointmentDate", target = "appointmentDate")
    @Mapping(source = "participants", target = "participants")
    @Mapping(source = "numberOfParticipants", target = "numberOfParticipants")
    @Mapping(source = "createdAt", target = "createdAt")
    RegistrationDTO toDTO(Registration registration);


    @Mapping(source = "serviceId", target = "service.id")
    @Mapping(source = "typeOfService", target = "typeOfService")
    @Mapping(source = "paymentStatus", target = "paymentStatus")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "appointmentDate", target = "appointmentDate")
    @Mapping(source = "participants", target = "participants")
    @Mapping(source = "numberOfParticipants", target = "numberOfParticipants")
    @Mapping(target = "createdAt", ignore = true)
    Registration toEntity(RegistrationDTO registrationDTO);

}