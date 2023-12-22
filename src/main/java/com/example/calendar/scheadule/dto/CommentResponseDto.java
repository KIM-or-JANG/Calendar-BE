package com.example.calendar.scheadule.dto;

import lombok.Getter;

@Getter
public class CommentResponseDto {
    private String locdate;
    private Long scheduleId;
    private String schedule;
    private Long commentId;
    private String comment;

    public CommentResponseDto(String locdate, Long scheduleId,String schedule, Long commentId, String comment) {
        this.locdate = locdate;
        this.scheduleId = scheduleId;
        this.schedule = schedule;
        this.commentId = commentId;
        this.comment = comment;
    }
}
