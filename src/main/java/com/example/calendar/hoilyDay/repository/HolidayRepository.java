package com.example.calendar.hoilyDay.repository;

import com.example.calendar.common.queryDSL.CustomHolidayRepository;
import com.example.calendar.hoilyDay.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long>, CustomHolidayRepository {

}
