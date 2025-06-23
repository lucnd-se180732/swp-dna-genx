package com.genx.mapper;

import com.genx.dto.response.AdnResultResponse;
import com.genx.entity.AdnResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdnResultMapper {
    AdnResultResponse toDto(AdnResult entity);
    AdnResult toEntity(AdnResultResponse dto);
}