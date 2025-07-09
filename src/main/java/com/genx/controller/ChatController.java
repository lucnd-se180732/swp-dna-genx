package com.genx.controller;

import com.genx.dto.request.MessageRequest;
import com.genx.dto.response.MessageResponse;
import com.genx.service.interfaces.IChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.security.Principal;

@Controller
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class ChatController {

    private final IChatService chatService;

    public ChatController(IChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public MessageResponse sendMessage(
            @DestinationVariable String roomId,
            MessageRequest request,
            Principal principal,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        try {
            // Get authenticated user from Principal (set by AuthChannelInterceptor)
            String authenticatedUser = principal != null ? principal.getName() : null;

            if (authenticatedUser == null) {
                throw new RuntimeException("User not authenticated");
            }

            return chatService.sendMessage(roomId, request, authenticatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message: " + e.getMessage());
        }
    }
}