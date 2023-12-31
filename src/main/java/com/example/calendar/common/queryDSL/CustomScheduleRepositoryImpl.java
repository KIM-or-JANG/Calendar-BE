package com.example.calendar.common.queryDSL;

import com.example.calendar.scheadule.dto.ScheduleListDto;
import com.example.calendar.scheadule.entity.QSchedule;
import com.example.calendar.scheadule.entity.Schedule;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomScheduleRepositoryImpl implements CustomScheduleRepository{

    private final JPAQueryFactory query;

    @Override
    public List<Schedule> findAllByRoom_IdAndLocdate(Long id, String yearMonth) {
        QSchedule schedule = QSchedule.schedule1;


        return query.selectFrom(schedule)
                .where(
                        schedule.room.Id.eq(id),
                        schedule.locdate.like(yearMonth + "%")
                )
                .fetch();
    }

    @Override
    public List<ScheduleListDto> QueryDSL_findAllByRoom_IdAndLocdate(Long id, String yearMonth) {
        QSchedule schedule = QSchedule.schedule1;


        return query.select(Projections.bean(ScheduleListDto.class, schedule.schedule, schedule.locdate, schedule.room.roomName, schedule.room.roomProfile))
                .from(schedule)
                .where(
                        schedule.room.Id.eq(id),
                        schedule.locdate.like(yearMonth + "%")
                )
                .fetch();
    }
}
