package com.genx.service.Impl;

import com.genx.dto.UserRequestDto;
import com.genx.dto.UserResponseDto;
import com.genx.entity.User;
import com.genx.enums.StaffType;
import com.genx.enums.Status;
import com.genx.enums.UserRole;
import com.genx.mapper.UserMapper;
import com.genx.repository.UserRepository;
import com.genx.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // tự động inject final fields
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto createStaff(UserRequestDto dto) {
        if (dto.getRole() == UserRole.GUEST || dto.getRole() == UserRole.CUSTOMER) {
            throw new IllegalArgumentException("Admin can only create STAFF or ADMIN accounts.");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        User staff = UserMapper.mapToUserEntity(dto);
        staff.setStatus(Status.ACTIVE); // Mặc định ACTIVE khi tạo

        User saved = userRepository.save(staff);
        return UserMapper.mapToUserResponseDto(saved);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.mapToUserResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::mapToUserResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setDisplayName(userRequestDto.getDisplayName());
        existingUser.setAvatar(userRequestDto.getAvatar());
        existingUser.setEmail(userRequestDto.getEmail());
        existingUser.setStaffType(userRequestDto.getStaffType());
        existingUser.setStatus(userRequestDto.getStatus());

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.mapToUserResponseDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public UserResponseDto updateStatus(Long id, Status status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(status);
        User updated = userRepository.save(user);
        return UserMapper.mapToUserResponseDto(updated);
    }

    @Override
    public List<UserResponseDto> getUsersByFilter(UserRole role, String statusStr, StaffType staffType) {
        List<User> users = userRepository.findAll();

        // Lọc theo role nếu có
        if (role != null) {
            users = users.stream()
                    .filter(u -> u.getRole() == role)
                    .collect(Collectors.toList());
        }

        // Nếu role là STAFF và có staffType thì lọc thêm
        if (role == UserRole.STAFF && staffType != null) {
            users = users.stream()
                    .filter(u -> u.getStaffType() == staffType)
                    .collect(Collectors.toList());
        }

        // Lọc theo status nếu có
        if (statusStr != null) {
            try {
                Status status = Status.valueOf(statusStr.toUpperCase());
                users = users.stream()
                        .filter(u -> u.getStatus() == status)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status value");
            }
        }

        return users.stream()
                .map(UserMapper::mapToUserResponseDto)
                .collect(Collectors.toList());
    }
}