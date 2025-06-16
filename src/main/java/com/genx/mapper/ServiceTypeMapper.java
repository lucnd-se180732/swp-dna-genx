package com.genx.mapper;

import com.genx.dto.ServiceTypeDto.ServiceTypeDTO;
import com.genx.entity.ServiceType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceTypeMapper {
    ServiceTypeDTO toDTO(ServiceType type);
    ServiceType toEntity(ServiceTypeDTO dto);
}