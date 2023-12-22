package com.example.calendar.scheadule.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long roomId;
    private Long scheduleId;
    private String comment;
}
