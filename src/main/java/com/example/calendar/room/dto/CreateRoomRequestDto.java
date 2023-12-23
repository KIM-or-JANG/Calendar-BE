package com.example.calendar.room.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateRoomRequestDto {
        private String roomName;
        private MultipartFile roomprofile;
}
