package com.example.calendar.scheadule.dto;

import com.example.calendar.room.entity.Room;
import com.example.calendar.room.entity.RoomUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MainResponseDto {
    private CalendarResponseDto calendarResponseDto;
    private List<RoomResponseDto> roomList;
}
