package com.example.calendar.social.kakao.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String nickName;
    private String email;
    private String profileImage;
    private String userBirthDay;

    public KakaoUserInfoDto(Long id, String username, String email, String profileImage) {
        this.id = id;
        this.nickName = username;
        this.email = email;
        this.profileImage = profileImage;
        this.userBirthDay = userBirthDay;
    }
}
