package com.example.calendar.Scheadule.repository;

import com.example.calendar.Scheadule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByroom_Id(Long id);
}
