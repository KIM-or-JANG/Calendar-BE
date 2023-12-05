package com.example.calendar.Scheadule.dto;

import com.example.calendar.Scheadule.entity.Schedule;
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
