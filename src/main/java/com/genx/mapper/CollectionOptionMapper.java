package com.genx.mapper;

import com.genx.dto.request.CollectionOptionRequest;
import com.genx.dto.response.CollectionOptionResponse;
import com.genx.entity.CollectionOption;
import com.genx.entity.ServiceType;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CollectionOptionMapper {

    @Mapping(target = "name", source = "req.name")
    @Mapping(target = "serviceType", source = "serviceType")
    CollectionOption toEntity(CollectionOptionRequest req, ServiceType serviceType);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "serviceTypeId", source = "serviceType.id")
    @Mapping(target = "serviceTypeName", source = "serviceType.name")
    CollectionOptionResponse toResponse(CollectionOption entity);
}
