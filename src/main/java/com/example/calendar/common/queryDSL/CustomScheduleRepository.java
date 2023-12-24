package com.example.calendar.common.queryDSL;


import com.example.calendar.scheadule.entity.Schedule;

import java.util.List;
import java.util.Optional;

public interface CustomScheduleRepository {
    List<Schedule> findAllByRoom_IdAndLocdate(Long id, String localdate);
}
