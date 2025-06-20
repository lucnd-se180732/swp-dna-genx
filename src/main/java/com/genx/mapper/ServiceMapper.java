package com.genx.mapper;

import com.genx.dto.ServiceTypeDto.ServiceTypeDTO;
import com.genx.entity.Service;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceTypeDTO toDTO(Service type);
    Service toEntity(ServiceTypeDTO dto);
}