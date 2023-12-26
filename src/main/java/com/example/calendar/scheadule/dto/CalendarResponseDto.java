package com.example.calendar.scheadule.dto;

import com.example.calendar.hoilyDay.dto.HoliyDayRequestDto;
import lombok.Getter;

import java.util.List;

@Getter
public class CalendarResponseDto {

    private List<HoliyDayRequestDto> holiday;
    private List<List<ScheduleListDto>> mainScheduleList;
    private List<ScheduleListDto> scheduleList;

    public CalendarResponseDto(List<HoliyDayRequestDto> holiydata, List<List<ScheduleListDto>> mainScheduleList, List<ScheduleListDto> scheduleList) {
        this.holiday = holiydata;
        this.mainScheduleList = mainScheduleList;
        this.scheduleList = scheduleList;
    }
}
