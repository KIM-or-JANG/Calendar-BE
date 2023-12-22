package com.example.calendar.scheadule.dto;

import lombok.Getter;

@Getter
public class CommentResponseDto {
    private String locdate;
    private Long scheduleId;
    private Long commentId;
    private String comment;

    public CommentResponseDto(String locdate, Long scheduleId, Long commentId, String comment) {
        this.locdate = locdate;
        this.scheduleId = scheduleId;
        this.commentId = commentId;
        this.comment = comment;
    }
}
