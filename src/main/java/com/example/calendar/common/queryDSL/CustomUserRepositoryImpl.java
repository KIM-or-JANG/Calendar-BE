package com.example.calendar.common.queryDSL;

import com.example.calendar.user.dto.UserResponseDto;
import com.example.calendar.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository{
    private final JPAQueryFactory query;

    @Override
    public List<UserResponseDto> findAllByEmail (String email){
        QUser user = QUser.user;

        return query.select(Projections.bean(UserResponseDto.class, user.email, user.nickName, user.profileImage))
                .from(user)
                .where(
                        user.email.eq(email)
                )
                .fetch();
    }
}
