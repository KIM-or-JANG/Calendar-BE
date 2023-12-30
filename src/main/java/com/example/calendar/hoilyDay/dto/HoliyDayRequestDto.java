package com.example.calendar.hoilyDay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HoliyDayRequestDto {
    private String dateName;
    private String locdate;



    public HoliyDayRequestDto(String dateName, String locdate) {
        this.dateName = dateName;
        this.locdate = locdate;
    }
}
