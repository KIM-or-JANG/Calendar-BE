package com.example.calendar.room.dto;

import lombok.Getter;

@Getter
public class InvateRoomResponseDto {
    private Long roomId;
    private String roomName;
    private Long userId;
    private String userName;

    public InvateRoomResponseDto(Long roomId, String roomName, Long userId, String userName) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.userId = userId;
        this.userName = userName;
    }
}
