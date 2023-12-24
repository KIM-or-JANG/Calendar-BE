package com.example.calendar.scheadule.dto;

import lombok.Getter;

@Getter
public class MyScheduleResponseDto {
    private String schedule;
    private String locdate;
    private MyRoomResponseDto room;

    public MyScheduleResponseDto(String schedule, String locdate, MyRoomResponseDto room) {
        this.schedule = schedule;
        this.locdate = locdate;
        this.room = room;
    }
}
