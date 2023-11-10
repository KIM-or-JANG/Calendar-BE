package com.example.calendar.repository;

import com.example.calendar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByuserName(String username);
}
