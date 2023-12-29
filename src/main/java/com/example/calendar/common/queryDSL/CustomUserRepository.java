package com.example.calendar.common.queryDSL;

import com.example.calendar.user.dto.UserResponseDto;

import java.util.List;

public interface CustomUserRepository {
    List<UserResponseDto> findAllByEmail (String email);
}
