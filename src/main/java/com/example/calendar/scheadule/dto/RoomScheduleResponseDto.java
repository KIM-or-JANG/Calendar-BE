package com.example.calendar.scheadule.dto;

import com.example.calendar.hoilyDay.dto.HoliyDayRequestDto;
import com.example.calendar.scheadule.entity.Schedule;
import lombok.Getter;

import java.util.List;

@Getter
public class RoomScheduleResponseDto {
    private Object holiyDayRequestDtoList;
    private List<Schedule> scheduleList;

    public RoomScheduleResponseDto(Object holiyDayRequestDtoList, List<Schedule> scheduleList) {
        this.holiyDayRequestDtoList = holiyDayRequestDtoList;
        this.scheduleList = scheduleList;
    }
}
