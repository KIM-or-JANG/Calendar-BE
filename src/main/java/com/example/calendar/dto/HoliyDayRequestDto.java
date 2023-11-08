package com.example.calendar.entity;

import lombok.Data;
import lombok.Getter;

@Data
public class HoliyDayRequestDto {
    private String dateKind;
    private String dateName;
    private String isHoliday;
    private String locdate;
    private String seq;

    public HoliyDayRequestDto(String dateKind, String dateName, String isHoliday, String locdate, String seq) {
        this.dateKind = dateKind;
        this.dateName = dateName;
        this.isHoliday = isHoliday;
        this.locdate = locdate;
        this.seq = seq;
    }
}
