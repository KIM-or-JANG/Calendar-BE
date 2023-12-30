package com.example.calendar.user.dto;

import com.example.calendar.user.entity.Friend;
import com.example.calendar.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendResponseDto {
    private String formUserEmail;
    private String fromUserNickName;
    private String fromUserProfileImage;
    private String toUserEmail;
    private String toUserNickName;
    private String toUserProfileImage;

    public FriendResponseDto(User fromUser, User toUser) {
        this.formUserEmail = fromUser.getEmail();
        this.fromUserNickName = fromUser.getNickName();
        this.fromUserProfileImage = fromUser.getProfileImage();
        this.toUserEmail = toUser.getEmail();
        this.toUserNickName = toUser.getNickName();
        this.toUserProfileImage = toUser.getProfileImage();
    }

    public void fromUser(String email, String nickName, String profileImage) {
        this.formUserEmail = email;
        this.fromUserNickName = nickName;
        this.fromUserProfileImage = profileImage;
    }
    public void toUser(String email, String nickname, String profileImage) {
        this.toUserEmail = email;
        this.toUserNickName = nickname;
        this.toUserProfileImage = profileImage;
    }

}
