package com.example.calendar.common.queryDSL;

import com.example.calendar.hoilyDay.dto.HoliyDayRequestDto;

import java.util.List;

public interface CustomHolidayRepository {
    List<HoliyDayRequestDto> findAllByYearAndMonth(String year, String month);
}
