package com.genx.mapper;

import com.genx.dto.request.UserCreationRequest;
import com.genx.dto.response.UserResponse;
import com.genx.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(com.genx.enums.ERole.CUSTOMER)")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "accountNonLocked", constant = "true")
    @Mapping(target = "authProvider", expression = "java(com.genx.enums.AuthProvider.SYSTEM)")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserCreationRequest dto);

    UserResponse toResponse(User user);
}
