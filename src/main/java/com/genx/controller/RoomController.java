package com.genx.controller;

import com.genx.dto.response.MessageResponse;
import com.genx.entity.Room;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.genx.service.interfaces.IChatService;
import com.genx.service.interfaces.IRoomService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class RoomController {

    private final IRoomService roomService;
    private final IChatService chatService;

    public RoomController(IRoomService roomService, IChatService chatService) {
        this.roomService = roomService;
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody Map<String, String> request, Authentication authentication) {
        try {
            String roomId = request.get("roomId");
            String customerId = request.get("customerId");
            String staffId = request.get("staffId");

            if (roomId == null || roomId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Room ID is required");
            }

            Room room = roomService.createRoom(roomId, customerId, staffId);
            return ResponseEntity.status(HttpStatus.CREATED).body(room);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId, Authentication authentication) {
        try {
            Room room = roomService.getRoomById(roomId);

            // Check if user can access this room
            String username = authentication.getName();
            if (!roomService.canUserAccessRoom(username, room)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            return ResponseEntity.ok(room);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(
            @PathVariable String roomId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            Authentication authentication
    ) {
        try {
            String username = authentication.getName();
            List<MessageResponse> messages = chatService.getMessagesByRoom(roomId, username, page, size);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<Room>> getUserRooms(Authentication authentication) {
        try {
            String userId = authentication.getName();
            List<Room> rooms = roomService.getUserRooms(userId);
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deactivateRoom(@PathVariable String roomId, Authentication authentication) {
        try {
            Room room = roomService.getRoomById(roomId);
            String username = authentication.getName();

            // Only staff can deactivate rooms
            if (!username.equals(room.getStaffId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only staff can deactivate rooms");
            }

            roomService.deactivateRoom(roomId);
            return ResponseEntity.ok().body("Room deactivated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}