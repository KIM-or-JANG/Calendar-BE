package com.example.calendar.repository;

import com.example.calendar.entity.RoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomUserRepository extends JpaRepository<RoomUser, Long> {

    List<RoomUser> findByuser_Id(Long id);
}
