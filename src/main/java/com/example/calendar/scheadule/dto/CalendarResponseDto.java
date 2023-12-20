package com.example.calendar.scheadule.dto;

import com.example.calendar.scheadule.entity.Schedule;
import lombok.Getter;

import java.util.List;

@Getter
public class CalendarResponseDto {

    private Object holiday;
    private List<Schedule> scheduleList;



    public CalendarResponseDto(Object holiydata, List<Schedule> schedules) {
        this.holiday = holiydata;
        this.scheduleList = schedules;
    }
}
