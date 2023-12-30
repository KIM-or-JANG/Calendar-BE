package com.example.calendar.common.queryDSL;

import com.example.calendar.user.entity.Friend;

import java.util.List;


public interface CustomFriendRepository {
     List<Friend> findByFromUserIDORToUserIDAndweAreFriend(Long userID, boolean weAreFriend);
}
