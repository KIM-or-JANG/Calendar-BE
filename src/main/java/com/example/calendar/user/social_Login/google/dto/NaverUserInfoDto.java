package com.example.calendar.user.social_Login.google.dto;

import lombok.Getter;

@Getter
public class NaverUserInfoDto {
    private Long id;
    private String nickName;
    private String email;
    private String profileImage;

    public NaverUserInfoDto(Long id, String nickname, String email, String profileImage) {
        this.id = id;
        this.nickName = nickname;
        this.email = email;
        this.profileImage = profileImage;
    }
}
