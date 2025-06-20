package com.genx.mapper;

import com.genx.dto.request.UserRequestDto;
import com.genx.dto.response.UserResponseDto;
import com.genx.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDto dto);
    UserResponseDto toDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget User user, UserRequestDto dto);
}