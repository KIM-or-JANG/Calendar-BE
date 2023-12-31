package com.example.calendar.scheadule.dto;

import com.example.calendar.room.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class RoomResponseDto {
    private Long id;
    private String roomName;
    private String roomProfile;

    public RoomResponseDto(Room room) {
        this.id = room.getId();
        this.roomName = room.getRoomName();
        this.roomProfile = room.getRoomProfile();
    }
}
