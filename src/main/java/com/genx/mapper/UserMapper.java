package com.genx.mapper;

import com.genx.dto.request.UserRequestDto;
import com.genx.dto.response.UserResponseDto;
import com.genx.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "staffInfo.avatar", target = "avatar")
    @Mapping(source = "staffInfo.fingerprintData", target = "fingerprintData")
    @Mapping(source = "staffInfo.startdDate", target = "startDate")
    UserResponseDto toDTO(User user);

    User toEntity(UserRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget User user, UserRequestDto dto);
}