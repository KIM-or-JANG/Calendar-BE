package com.example.calendar.common.queryDSL;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomScheduleReositoryImpl implements CustomScheduleRepository{

    private final JPAQueryFactory query;

}
