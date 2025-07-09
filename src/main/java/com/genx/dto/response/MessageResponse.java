package com.genx.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private Long id;
    private String content;
    private String sender;
    private String roomId;
    private LocalDateTime timeStamp;

    // Constructor without id for creating new responses
    public MessageResponse(String content, String sender, String roomId, LocalDateTime timeStamp) {
        this.content = content;
        this.sender = sender;
        this.roomId = roomId;
        this.timeStamp = timeStamp;
    }
}