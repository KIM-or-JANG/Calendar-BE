package com.example.calendar.room.repository;

import com.example.calendar.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByIdAndRoomName(Long roomId, String roomName);
}
