package com.example.calendar.common.queryDSL;


import com.example.calendar.scheadule.dto.ScheduleListDto;
import com.example.calendar.scheadule.entity.Schedule;

import java.util.List;


public interface CustomScheduleRepository {
    List<Schedule> findAllByRoom_IdAndLocdate(Long id, String localdate);
    List<ScheduleListDto> QueryDSL_findAllByRoom_IdAndLocdate(Long id, String localdate);
}
