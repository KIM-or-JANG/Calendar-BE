package com.example.calendar.hoilyDay.entity;

import com.example.calendar.hoilyDay.dto.HoliyDayRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String dateName;

    @Column(unique = true) //중복 방지를 위한 유니크 제약
    private String locdate;

    public Holiday(HoliyDayRequestDto holiyDayRequestDto) {
        this.dateName = holiyDayRequestDto.getDateName();
        this.locdate = holiyDayRequestDto.getLocdate();
    }
}
