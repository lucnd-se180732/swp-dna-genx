package com.genx.service.impl;

import com.genx.entity.Room;
import com.genx.repository.IRoomRepository;
import com.genx.service.interfaces.IRoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoomServiceImpl implements IRoomService {

    private final IRoomRepository roomRepository;

    public RoomServiceImpl(IRoomRepository roomRepository) {  // Fixed constructor name
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(String roomId, String customerId, String staffId) {
        if (roomRepository.findByRoomId(roomId) != null) {
            throw new RuntimeException("Room already exists with ID: " + roomId);
        }

        Room room = new Room();
        room.setRoomId(roomId);
        room.setCustomerId(customerId);
        room.setStaffId(staffId);

        return roomRepository.save(room);
    }

    @Override
    public Room getRoomById(String roomId) {
        Room room = roomRepository.findByRoomId(roomId);
        if (room == null) {
            throw new RuntimeException("Room not found: " + roomId);
        }
        return room;
    }

    @Override
    public List<Room> getUserRooms(String userId) {
        return roomRepository.findUserRooms(userId);
    }

    @Override
    public List<Room> getActiveRooms() {
        return roomRepository.findByIsActiveTrueOrderByLastMessageAtDesc();
    }

    @Override
    public void deactivateRoom(String roomId) {
        Room room = getRoomById(roomId);
        room.setIsActive(false);
        roomRepository.save(room);
    }

    @Override
    public boolean canUserAccessRoom(String username, Room room) {
        return username.equals(room.getCustomerId()) || username.equals(room.getStaffId());
    }
}