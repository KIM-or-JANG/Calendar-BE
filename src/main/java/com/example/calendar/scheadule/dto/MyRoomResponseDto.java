package com.example.calendar.scheadule.dto;

import lombok.Getter;

@Getter
public class MyRoomResponseDto {
    private Long roomId;
    private String roomName;
    private String roomProfile;

    public MyRoomResponseDto(Long roomId, String roomName, String roomProfile) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomProfile = roomProfile;
    }
}
