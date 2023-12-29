package com.example.calendar.user.repository;

import com.example.calendar.common.queryDSL.CustomUserRepository;
import com.example.calendar.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    Optional<User> findByEmail(String email);
    Optional<User> findByKakaoId(Long kakaoId);
    Optional<User> findByIdAndEmail(Long userId, String userEmail);
    void deleteByIdAndEmail(Long userId, String email);
}
