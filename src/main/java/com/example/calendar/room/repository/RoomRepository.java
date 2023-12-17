package com.example.calendar.room.repository;

import com.example.calendar.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Room findByIdAndRoomName(Long roomId, String roomName);
}
