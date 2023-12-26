package com.example.calendar.scheadule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleListDto {
    private String locdate;
    private String schedule;
    private String roomName;
    private String roomProfile;
}
