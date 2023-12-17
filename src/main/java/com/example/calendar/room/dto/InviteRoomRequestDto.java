package com.example.calendar.room.dto;

import lombok.Getter;

@Getter
public class InviteRoomRequestDto {
    private Long roomId;
    private String roomName;
    private Long userId;
    private String userName;
    private String userEmail;
}
