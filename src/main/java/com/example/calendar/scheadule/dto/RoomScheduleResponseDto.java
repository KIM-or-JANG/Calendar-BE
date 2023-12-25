package com.example.calendar.scheadule.dto;

import com.example.calendar.hoilyDay.dto.HoliyDayRequestDto;
import com.example.calendar.scheadule.entity.Schedule;

import java.util.List;

public class RoomScheduleResponseDto {
    private List<HoliyDayRequestDto> holiyDayRequestDtoList;
    private List<Schedule> scheduleList;

    public RoomScheduleResponseDto(List<HoliyDayRequestDto> holiyDayRequestDtoList, List<Schedule> scheduleList) {
        this.holiyDayRequestDtoList = holiyDayRequestDtoList;
        this.scheduleList = scheduleList;
    }
}
