package com.example.calendar.room.service;

import com.example.calendar.common.exception.CustomException;
import com.example.calendar.common.exception.ErrorCode;
import com.example.calendar.common.security.userDetails.UserDetailsImpl;
import com.example.calendar.common.util.Message;
import com.example.calendar.room.dto.CreateRoomRequestDto;
import com.example.calendar.room.dto.CreateRoomResponseDto;
import com.example.calendar.room.dto.InvateRoomResponseDto;
import com.example.calendar.room.dto.InviteRoomRequestDto;
import com.example.calendar.room.entity.Room;
import com.example.calendar.room.entity.RoomUser;
import com.example.calendar.room.repository.RoomRepository;
import com.example.calendar.room.repository.RoomUserRepository;
import com.example.calendar.user.entity.User;
import com.example.calendar.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomUserRepository roomUserRepository;
    private final UserRepository userRepository;
    //방 만들기
    @Transactional
    public ResponseEntity<Message> createRoom(CreateRoomRequestDto roomRequestDto, UserDetailsImpl userDetails) {
        String roomProfile = null;
        if(roomRequestDto.getRoomprofile() == null) {
            roomProfile = roomRequestDto.getRoomName();
        }
        else {
//            s3사용
        }
        Room room = roomRepository.saveAndFlush(new Room(roomRequestDto.getRoomName(), roomProfile, userDetails.getUser()));
        roomUserRepository.saveAndFlush(new RoomUser(room, true));
        CreateRoomResponseDto createRoomResponseDto = new CreateRoomResponseDto(
                room.getId(), room.getRoomName(),  room.getRoomProfile(), room.getManager().getId(), room.getManager().getNickName()
        );
        return new ResponseEntity<>(new Message("방 생성 성공",createRoomResponseDto), HttpStatus.OK);
    }
    //방 초대(URL제공으로 초대 or 방장이 직접 초대 or 타인이 방에게 초대 메세지 보내기(방장이 허용) ) = 방장이 직접 초대
    @Transactional
    public ResponseEntity<Message> inviteUser(InviteRoomRequestDto inviteRoomRequestDto, UserDetailsImpl userDetails) {
        Room room = roomRepository.findByIdAndRoomName(inviteRoomRequestDto.getRoomId(), inviteRoomRequestDto.getRoomName())
                .orElseThrow(
                        () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
                );
        if(room.getManager().getId() == userDetails.getUser().getId()) {
            User user = userRepository.findByIdAndEmail(inviteRoomRequestDto.getUserId(), inviteRoomRequestDto.getUserEmail())
                    .orElseThrow(
                            () -> new CustomException(ErrorCode.USER_NOT_FOUND)
                    );
            roomUserRepository.saveAndFlush(new RoomUser(user, room, false));
            InvateRoomResponseDto invateRoomResponseDto = new InvateRoomResponseDto(room.getId(), room.getRoomName(), user.getId(), user.getNickName());
            return new ResponseEntity<>(new Message("방 초대 성공",invateRoomResponseDto),HttpStatus.OK);
        }
        else {
            throw new CustomException(ErrorCode.FORBIDDEN_MANAGER);
        }
    }
    //방 삭제
    @Transactional
    public ResponseEntity<Message> deleteRoom(Long id, String roomName, UserDetailsImpl userDetails) {
        Room room = roomRepository.findByIdAndRoomName(id,roomName).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        if(room.getManager().getId() == userDetails.getUser().getId()){
            roomUserRepository.deleteAllByroom_Id(room.getId());
            roomRepository.deleteByIdAndRoomName(id, roomName);
        } else {
            throw new CustomException(ErrorCode.FORBIDDEN_MANAGER);
        }
        CreateRoomResponseDto createRoomResponseDto = new CreateRoomResponseDto(
                room.getId(), room.getRoomName(),  room.getRoomProfile(), room.getManager().getId(), room.getManager().getNickName()
        );
        return new ResponseEntity<>(new Message("방이 삭제 되었습니다.",createRoomResponseDto),HttpStatus.OK);
    }

}
