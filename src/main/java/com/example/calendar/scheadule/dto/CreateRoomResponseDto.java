package com.example.calendar.scheadule.dto;

import lombok.Getter;

@Getter
public class CreateRoomResponseDto {
    private Long roomId;
    private String roomName;
    private String roomProfile;
    private Long managerUserId;
    private String managerUserName;

    public CreateRoomResponseDto(Long id, String roomName,String roomProfile, Long userid, String nickName) {
        this.roomId = id;
        this.roomName = roomName;
        this.roomProfile = roomProfile;
        this.managerUserId = userid;
        this.managerUserName = nickName;
    }
}
