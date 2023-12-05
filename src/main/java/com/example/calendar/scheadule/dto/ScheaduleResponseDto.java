package com.example.calendar.scheadule.dto;

import com.example.calendar.scheadule.entity.Schedule;
import lombok.Getter;

import java.util.List;

@Getter
public class ScheaduleResponseDto {

    private Object holiday;
    private List<Schedule> scheduleList;



    public ScheaduleResponseDto(Object holiydata, List<Schedule> schedules) {
        this.holiday = holiydata;
        this.scheduleList = schedules;
    }
}
