package com.genx.mapper;

import com.genx.dto.UserRequestDto;
import com.genx.dto.UserResponseDto;
import com.genx.entity.User;

public class UserMapper {

    public static User mapToUserEntity(UserRequestDto dto) {
        if (dto == null) return null;

        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .displayName(dto.getDisplayName())
                .avatar(dto.getAvatar())
                .role(dto.getRole())
                .staffType(dto.getStaffType())
                .status(dto.getStatus())
                .build();
    }

    public static UserResponseDto mapToUserResponseDto(User entity) {
        if (entity == null) return null;

        return UserResponseDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .displayName(entity.getDisplayName())
                .avatar(entity.getAvatar())
                .role(entity.getRole())
                .staffType(entity.getStaffType())
                .status(entity.getStatus())
                .build();
    }

    public static void updateUserEntity(User entity, UserRequestDto dto) {
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setEmail(dto.getEmail());
        entity.setDisplayName(dto.getDisplayName());
        entity.setAvatar(dto.getAvatar());
        entity.setRole(dto.getRole());
        entity.setStaffType(dto.getStaffType());
        entity.setStatus(dto.getStatus());
    }
}