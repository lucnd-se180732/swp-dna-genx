package com.genx.controller;

import com.genx.dto.UserRequestDto;
import com.genx.dto.UserResponseDto;
import com.genx.enums.ERole;
import com.genx.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final IUserService userService;

    @PostMapping("/staff")
    public ResponseEntity<UserResponseDto> createStaff(@RequestBody UserRequestDto request) {
        if (request.getRole() != ERole.LAB_STAFF && request.getRole() != ERole.RECORD_STAFF && request.getRole() != ERole.ADMIN) {
            return ResponseEntity.badRequest().build();
        }
        UserResponseDto response = userService.createStaff(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/staff/{id}")
    public ResponseEntity<UserResponseDto> updateStaff(@PathVariable Long id, @RequestBody UserRequestDto request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<UserResponseDto> updateStatus(@PathVariable Long id,
                                                        @RequestParam boolean enabled,
                                                        @RequestParam boolean accountNonLocked) {
        return ResponseEntity.ok(userService.updateUserStatus(id, enabled, accountNonLocked));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/staff/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        if (user.getRole() != ERole.LAB_STAFF && user.getRole() != ERole.RECORD_STAFF && user.getRole() != ERole.ADMIN) {
            return ResponseEntity.status(403).build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/filter")
    public ResponseEntity<List<UserResponseDto>> getUsersByFilter(
            @RequestParam(required = false) ERole role,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) Boolean accountNonLocked) {
        return ResponseEntity.ok(userService.getUsersByFilter(role, enabled, accountNonLocked));
    }
}