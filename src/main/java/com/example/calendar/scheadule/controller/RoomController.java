package com.example.calendar.scheadule.controller;

import com.example.calendar.common.security.userDetails.UserDetailsImpl;
import com.example.calendar.common.util.Message;
import com.example.calendar.scheadule.dto.CreateRoomRequestDto;
import com.example.calendar.scheadule.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    //방 만들기
    @PostMapping(value = "/create/room", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> createRoom(@ModelAttribute CreateRoomRequestDto roomRequestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return roomService.createRoom(roomRequestDto, userDetails);
    }
}
