package com.genx.service.Impl;

import com.genx.dto.request.UserRequestDto;
import com.genx.dto.response.UserResponseDto;
import com.genx.entity.User;
import com.genx.enums.ERole;
import com.genx.mapper.UserMapper;
import com.genx.repository.UserRepository;
import com.genx.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto createStaff(UserRequestDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        User staff = userMapper.toEntity(dto);
        User saved = userRepository.save(staff);
        return userMapper.toDTO(saved);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDTO(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateEntity(user, dto);
        User updated = userRepository.save(user);
        return userMapper.toDTO(updated);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public UserResponseDto updateUserStatus(Long id, boolean enabled, boolean accountNonLocked) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(enabled);
        user.setAccountNonLocked(accountNonLocked);
        User updated = userRepository.save(user);
        return userMapper.toDTO(updated);
    }

    @Override
    public List<UserResponseDto> getUsersByFilter(ERole role, Boolean enabled, Boolean accountNonLocked) {
        return userRepository.findAll().stream()
                .filter(u -> role == null || u.getRole() == role)
                .filter(u -> enabled == null || u.isEnabled() == enabled)
                .filter(u -> accountNonLocked == null || u.isAccountNonLocked() == accountNonLocked)
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}