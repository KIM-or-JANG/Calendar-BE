package com.example.calendar.user.repository;

import com.example.calendar.common.queryDSL.CustomFriendRepository;
import com.example.calendar.user.entity.Friend;
import com.example.calendar.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long>, CustomFriendRepository {
        Optional<Friend> findByFromUserIDAndToUserID(User id, User id1);
}
