package com.example.calendar.scheadule.dto;

import com.example.calendar.hoilyDay.dto.HoliyDayRequestDto;
import lombok.Getter;

import java.util.List;

@Getter
public class CalendarResponseDto {

    private List<HoliyDayRequestDto> holiday;
    private List<ScheduleListDto> scheduleList;

    public CalendarResponseDto(List<HoliyDayRequestDto> holiydata, List<ScheduleListDto> schedules) {
        this.holiday = holiydata;
        this.scheduleList = schedules;
    }
}
