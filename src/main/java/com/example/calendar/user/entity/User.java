package com.example.calendar.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column
    private String profileImage;

    // soft delete
    @Column(nullable = false)
    private boolean isDeleted = false;

    private Long kakaoId;

    private Long naverId;

    private Long googleId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany
    @JoinColumn(name = "friend_id")
    private List<Friend> friendList = new ArrayList<>();

    //소셜 회원가입
    public User(Long kakaoId, Long naverId, Long googleId, String email, String nickname, String password, String profileImage, UserRoleEnum role) {
        this.kakaoId = kakaoId;
        this.naverId = naverId;
        this.googleId = googleId;
        this.email = email;
        this.nickName = nickname;
        this.password = password;
        this.profileImage = profileImage;
        this.role = role;
    }
    //userUpdate
    public User update(String name,String email ,String profileImage) {
        this.nickName = name;
        this.email = email;
        this.profileImage = profileImage;
        return this;
    }

    public String getRoleKey() {
        return this.role.getAuthority();
    }
    //KakaoLogin
    public User kakaoUpdate(Long kakaoId, String profileImage) {
        this.kakaoId = kakaoId;
        this.profileImage = profileImage;
        return this;
    }
}
