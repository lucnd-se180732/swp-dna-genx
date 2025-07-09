package com.genx.service.impl;

import com.genx.dto.request.MessageRequest;
import com.genx.dto.response.MessageResponse;
import com.genx.entity.Message;
import com.genx.entity.Room;
import com.genx.repository.IMessageRepository;
import com.genx.repository.IRoomRepository;
import com.genx.service.interfaces.IChatService;  // Updated import
import com.genx.service.interfaces.IRoomService;  // Updated import
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatServiceImpl implements IChatService {

    private final IMessageRepository messageRepository;
    private final IRoomRepository roomRepository;
    private final IRoomService roomService;

    public ChatServiceImpl(IMessageRepository messageRepository, IRoomRepository roomRepository, IRoomService roomService) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.roomService = roomService;
    }

    @Override
    public MessageResponse sendMessage(String roomId, MessageRequest request, String authenticatedUser) {
        Room room = roomRepository.findByRoomId(roomId);
        if (room == null) {
            throw new RuntimeException("Room not found: " + roomId);
        }

        // Verify user can send message to this room
        if (!roomService.canUserAccessRoom(authenticatedUser, room)) {
            throw new RuntimeException("User not authorized to send message to this room");
        }

        Message message = new Message();
        message.setContent(request.getContent());
        message.setSender(authenticatedUser);
        message.setTimeStamp(LocalDateTime.now());
        message.setRoom(room);

        Message savedMessage = messageRepository.save(message);

        // Update room's last message time
        room.setLastMessageAt(LocalDateTime.now());
        roomRepository.save(room);

        return new MessageResponse(
                savedMessage.getId(),
                savedMessage.getContent(),
                savedMessage.getSender(),
                roomId,
                savedMessage.getTimeStamp()
        );
    }

    @Override
    public List<MessageResponse> getMessagesByRoom(String roomId, String authenticatedUser, int page, int size) {
        Room room = roomRepository.findByRoomId(roomId);
        if (room == null) {
            throw new RuntimeException("Room not found: " + roomId);
        }

        // Verify user can access this room
        if (!roomService.canUserAccessRoom(authenticatedUser, room)) {
            throw new RuntimeException("User not authorized to access this room");
        }

        Pageable pageable = PageRequest.of(page, size);
        List<Message> messages = messageRepository.findByRoomRoomIdOrderByTimeStampDesc(roomId);

        return messages.stream()
                .map(msg -> new MessageResponse(
                        msg.getId(),
                        msg.getContent(),
                        msg.getSender(),
                        roomId,
                        msg.getTimeStamp()
                ))
                .collect(Collectors.toList());
    }
}