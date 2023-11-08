package com.example.calendar.dto;

import lombok.Data;

@Data
public class HoliyDayRequestDto {
    private String dateName;
    private String locdate;



    public HoliyDayRequestDto(String dateName, String locdate) {
        this.dateName = dateName;
        this.locdate = locdate;
    }
}
