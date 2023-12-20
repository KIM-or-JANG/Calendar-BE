package com.example.calendar.scheadule.dto;

import lombok.Getter;

@Getter
public class CalendarRequestDto {

    private String dateTitle;

    private String date;

    private String month;

    private String year;

    public CalendarRequestDto(String dateTitle, String date, String month, String year) {
        this.dateTitle = dateTitle;
        this.date = date;
        this.month = month;
        this.year = year;
    }
}
