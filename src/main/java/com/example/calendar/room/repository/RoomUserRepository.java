package com.example.calendar.room.repository;

import com.example.calendar.room.entity.RoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomUserRepository extends JpaRepository<RoomUser, Long> {
    List<RoomUser> findByuser_Id(Long id);
    Optional<RoomUser> findByuser_IdAndRoom_Id(Long userId, Long roomId);
}
