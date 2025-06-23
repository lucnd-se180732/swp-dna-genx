package com.genx.service.interfaces;

import com.genx.dto.request.UserRequestDto;
import com.genx.dto.response.UserResponseDto;
import com.genx.enums.ERole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    UserResponseDto createStaff(UserRequestDto dto);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUser(Long id, UserRequestDto dto);
    void deleteUser(Long id);
    UserResponseDto updateUserStatus(Long id, boolean enabled, boolean accountNonLocked);
    Page<UserResponseDto> getUsersByFilter(ERole role, Boolean enabled, Boolean accountNonLocked, Pageable pageable);

}