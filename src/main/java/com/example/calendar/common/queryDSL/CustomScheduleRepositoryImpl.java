package com.example.calendar.common.queryDSL;

import com.example.calendar.scheadule.entity.QSchedule;
import com.example.calendar.scheadule.entity.Schedule;
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
}
