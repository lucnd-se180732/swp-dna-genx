package com.genx.service.interfaces;

import com.genx.entity.Room;
import java.util.List;

public interface IRoomService {
    Room createRoom(String roomId, String customerId, String staffId);
    Room getRoomById(String roomId);
    List<Room> getUserRooms(String userId);
    List<Room> getActiveRooms();
    void deactivateRoom(String roomId);
    boolean canUserAccessRoom(String username, Room room);
}