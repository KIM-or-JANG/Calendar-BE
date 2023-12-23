package com.example.calendar.room.controller;

import com.example.calendar.common.security.userDetails.UserDetailsImpl;
import com.example.calendar.common.util.Message;
import com.example.calendar.room.dto.CreateRoomRequestDto;
import com.example.calendar.room.dto.InviteRoomRequestDto;
import com.example.calendar.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    //방 만들기
    @PostMapping(value = "/create/room", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> createRoom(@ModelAttribute CreateRoomRequestDto roomRequestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return roomService.createRoom(roomRequestDto, userDetails);
    }
    //방 초대
    @PostMapping("invite/user")
    public ResponseEntity<Message> inviteUser (@RequestBody InviteRoomRequestDto inviteUserDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails){
        return roomService.inviteUser(inviteUserDto, userDetails);
    }
    //방 삭제
    @DeleteMapping("delete/room")
    public ResponseEntity<Message> deleteRoom(@RequestParam Long id,
                                              @RequestParam String roomName ,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails ) throws IOException {
        return roomService.deleteRoom(id,roomName, userDetails);
    }
}
