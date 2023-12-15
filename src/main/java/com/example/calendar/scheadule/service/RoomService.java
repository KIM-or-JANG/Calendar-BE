package com.example.calendar.scheadule.service;

import com.example.calendar.common.security.userDetails.UserDetailsImpl;
import com.example.calendar.common.util.Message;
import com.example.calendar.scheadule.dto.CreateRoomRequestDto;
import com.example.calendar.scheadule.dto.CreateRoomResponseDto;
import com.example.calendar.scheadule.entity.Room;
import com.example.calendar.scheadule.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public ResponseEntity<Message> createRoom(CreateRoomRequestDto roomRequestDto, UserDetailsImpl userDetails) {
        Room room = new Room(roomRequestDto.getRoomName(), roomRequestDto.getRoomprofile(), userDetails.getUser());
        roomRepository.saveAndFlush(room);
        CreateRoomResponseDto createRoomResponseDto = new CreateRoomResponseDto(
                room.getId(), room.getRoomName(),  room.getRoomProfile(), room.getManager().getId(), room.getManager().getNickName()
        );
        return new ResponseEntity<>(new Message("방 생성 성공",createRoomResponseDto), HttpStatus.OK);
    }
}
