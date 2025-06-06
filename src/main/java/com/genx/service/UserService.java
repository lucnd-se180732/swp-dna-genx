package com.genx.service;

import com.genx.dto.UserRequestDto;
import com.genx.dto.UserResponseDto;
import com.genx.enums.StaffType;
import com.genx.enums.Status;
import com.genx.enums.UserRole;

import java.util.List;

public interface UserService {
    UserResponseDto createStaff(UserRequestDto userRequestDto);

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);

    void deleteUser(Long id);


    UserResponseDto updateStatus(Long id, Status status);

    List<UserResponseDto> getUsersByFilter(UserRole role, Status status, StaffType staffType);
}