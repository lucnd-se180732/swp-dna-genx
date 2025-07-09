package com.genx.service.interfaces;

import com.genx.dto.request.MessageRequest;
import com.genx.dto.response.MessageResponse;
import java.util.List;

public interface IChatService {
    MessageResponse sendMessage(String roomId, MessageRequest request, String authenticatedUser);
    List<MessageResponse> getMessagesByRoom(String roomId, String authenticatedUser, int page, int size);
}