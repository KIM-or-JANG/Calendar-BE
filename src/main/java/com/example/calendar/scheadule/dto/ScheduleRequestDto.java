package com.example.calendar.scheadule.dto;

import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    private Long roomId;
    private String roomName;
    private String locdate;
    private String schedule;
}
