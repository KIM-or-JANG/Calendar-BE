package com.example.calendar.scheadule.repository;

import com.example.calendar.common.queryDSL.CustomScheduleRepository;
import com.example.calendar.scheadule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, CustomScheduleRepository {
//    List<Schedule> findByroom_Id(Long id);

    Optional<Schedule> findByIdAndRoom_IdAndUser_IdAndLocdate(Long scheduleId, Long roomId, Long id, String locdate);

    Optional<Schedule> findByIdAndRoom_IdAndLocdate(Long scheduleId, Long roomId, String locdate);
}
