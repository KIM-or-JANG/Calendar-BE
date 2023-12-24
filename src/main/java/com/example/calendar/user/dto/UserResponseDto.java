package com.example.calendar.user.dto;

import lombok.Getter;

@Getter
public class UserResponseDto {
    private String email;
    private String profileImage;
    private String nickName;

    public UserResponseDto(String email, String profileImage, String nickName) {
        this.email = email;
        this.profileImage = profileImage;
        this.nickName = nickName;
    }
}
