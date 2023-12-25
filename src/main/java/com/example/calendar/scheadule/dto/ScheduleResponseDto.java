package com.example.calendar.scheadule.dto;

import com.example.calendar.scheadule.entity.Comment;
import lombok.Getter;

import java.util.List;

@Getter
public class ScheduleResponseDto {
    private Long roomId;
    private Long scheduleId;
    private String roomName;
    private String locdate;
    private String schedule;
    private List<CommentResponseDto> commentList;

    public ScheduleResponseDto(Long scheduleId, Long roomId, String roomName, String locdate, String schedule) {
        this.scheduleId = scheduleId;
        this.roomId = roomId;
        this.roomName = roomName;
        this.locdate = locdate;
        this.schedule = schedule;
    }

    public ScheduleResponseDto(Long scheduleId, Long roomId, String roomName, String locdate, String schedule, List<CommentResponseDto> commentList) {
        this.roomId = roomId;
        this.scheduleId = scheduleId;
        this.roomName = roomName;
        this.locdate = locdate;
        this.schedule = schedule;
        this.commentList = commentList;
    }
}
