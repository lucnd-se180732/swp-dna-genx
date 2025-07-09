package com.genx.repository;

import com.genx.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoomRepository extends JpaRepository<Room, Long> {
    Room findByRoomId(String roomId);

    List<Room> findByCustomerId(String customerId);

    List<Room> findByStaffId(String staffId);

    @Query("SELECT r FROM Room r WHERE r.customerId = :userId OR r.staffId = :userId")
    List<Room> findUserRooms(@Param("userId") String userId);

    List<Room> findByIsActiveTrueOrderByLastMessageAtDesc();
}