package com.example.calendar.scheadule.dto;

import lombok.Getter;

@Getter
public class ScheduleResponseDto {
    private Long roomId;
    private Long scheduleId;
    private String roomName;
    private String locdate;
    private String schedule;

    public ScheduleResponseDto(Long scheduleId, Long roomId, String roomName, String locdate, String schedule) {
        this.scheduleId = scheduleId;
        this.roomId = roomId;
        this.roomName = roomName;
        this.locdate = locdate;
        this.schedule = schedule;
    }
}
