
package com.genx.controller;

import com.genx.dto.UserRequestDto;
import com.genx.dto.UserResponseDto;
import com.genx.enums.StaffType;
import com.genx.enums.Status;
import com.genx.enums.UserRole;
import com.genx.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.genx.enums.Status.BANNED;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    // Admin creates staff
    @PostMapping("/staff")
    public ResponseEntity<UserResponseDto> createStaff(@RequestBody UserRequestDto request) {
        if (request.getRole() != UserRole.STAFF && request.getRole() != UserRole.ADMIN) {
            return ResponseEntity.badRequest().build();
        }
        UserResponseDto response = userService.createStaff(request);
        return ResponseEntity.ok(response);
    }

    // Admin updates staff info
    @PutMapping("/staff/{id}")
    public ResponseEntity<UserResponseDto> updateStaff(@PathVariable Long id, @RequestBody UserRequestDto request) {
        UserResponseDto response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    // Admin bans a user (sets status to BANNED)
    @PutMapping("/ban/{id}")
    public ResponseEntity<UserResponseDto> banUser(@PathVariable Long id) {
        UserResponseDto banned = userService.updateStatus(id, BANNED);
        return ResponseEntity.ok(banned);
    }

    // Admin gets all users (for viewing)
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Admin deletes STAFF only (not GUEST or CUSTOMER)
    @DeleteMapping("/staff/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        if (user.getRole() != UserRole.STAFF && user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(403).build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/filter")
    public ResponseEntity<List<UserResponseDto>> getUsersByFilter(
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) StaffType staffType
    ) {
        return ResponseEntity.ok(userService.getUsersByFilter(role, status, staffType));
    }
}



