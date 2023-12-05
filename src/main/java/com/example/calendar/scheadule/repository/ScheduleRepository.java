package com.example.calendar.scheadule.repository;

import com.example.calendar.scheadule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByroom_Id(Long id);
}
