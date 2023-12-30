package com.example.calendar.common.queryDSL;

import com.example.calendar.scheadule.entity.QSchedule;
import com.example.calendar.user.entity.Friend;
import com.example.calendar.user.entity.QFriend;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomFriendRepositoryImpl implements CustomFriendRepository{
    private final JPAQueryFactory query;


    @Override
    public List<Friend> findByFromUserIDORToUserIDAndweAreFriend(Long userID, boolean weAreFriend) {
        QFriend friend = QFriend.friend;

        BooleanExpression condition = friend.fromUserID.id.eq(userID)
                .or(friend.toUserID.id.eq(userID))
                .and(friend.weAreFriend.eq(weAreFriend));

        return query.selectFrom(friend)
                .where(condition)
                .fetch();
    }
}
