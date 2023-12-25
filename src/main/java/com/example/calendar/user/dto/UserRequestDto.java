package com.example.calendar.user.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserRequestDto {
    private String profileImage;
    private MultipartFile NewProfileImage;
    private String nickName;
}
