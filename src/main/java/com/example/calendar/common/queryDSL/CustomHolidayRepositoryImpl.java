package com.example.calendar.common.queryDSL;

import com.example.calendar.hoilyDay.dto.HoliyDayRequestDto;
import com.example.calendar.hoilyDay.entity.QHoliday;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomHolidayRepositoryImpl implements CustomHolidayRepository{
    private final JPAQueryFactory query;

    @Override
    public List<HoliyDayRequestDto> findAllByYearAndMonth(String year, String month){
        QHoliday holiday = QHoliday.holiday;
        String locdate = year + month;
        return query.select(Projections.bean(HoliyDayRequestDto.class, holiday.dateName, holiday.locdate))
                .from(holiday)
                .where(
                        holiday.locdate.like(locdate + "%")
                )
                .fetch();
    }
}
