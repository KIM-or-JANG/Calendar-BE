package com.example.calendar.user.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserRequestDto {
//    private Long userID;
    private String email;
    private String profileImage;
    private MultipartFile NewProfileImage;
    private String nickName;
}
