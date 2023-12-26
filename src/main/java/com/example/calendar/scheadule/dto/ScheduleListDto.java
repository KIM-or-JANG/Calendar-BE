package com.example.calendar.scheadule.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Getter
public class ScheduleListDto {
    private String locdate;
    private String schedule;
    private String roomName;
    private String roomProfile;

    public ScheduleListDto(String locdate, String schedule, String roomName, String roomProfile) {
        this.locdate = locdate;
        this.schedule = schedule;
        this.roomName = roomName;
        this.roomProfile = roomProfile;
    }

    public ScheduleListDto(String locdate, String schedule) {
        this.locdate = locdate;
        this.schedule = schedule;
    }
}
