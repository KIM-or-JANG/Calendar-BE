package com.example.calendar.scheadule.service;

import com.example.calendar.common.exception.CustomException;
import com.example.calendar.common.exception.ErrorCode;
import com.example.calendar.common.security.userDetails.UserDetailsImpl;
import com.example.calendar.common.util.Message;
import com.example.calendar.room.entity.Room;
import com.example.calendar.room.repository.RoomRepository;
import com.example.calendar.room.repository.RoomUserRepository;
import com.example.calendar.scheadule.dto.CommentRequestDto;
import com.example.calendar.scheadule.dto.CommentResponseDto;
import com.example.calendar.scheadule.entity.Comment;
import com.example.calendar.scheadule.entity.Schedule;
import com.example.calendar.scheadule.repository.CommentRepository;
import com.example.calendar.scheadule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
        private final RoomRepository roomRepository;
        private final RoomUserRepository roomUserRepository;
        private final ScheduleRepository scheduleRepository;
        private final CommentRepository commentRepository;
    //댓글 작성
    public ResponseEntity<Message> createComment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        Room room = roomRepository.findById(commentRequestDto.getRoomId()).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        roomUserRepository.findByuser_IdAndRoom_Id(userDetails.getUser().getId(), room.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.FORBIDDEN_MEMBER)
        );
        Schedule schedule = scheduleRepository.findById(commentRequestDto.getScheduleId()).orElseThrow(
                () -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND)
        );
        Comment comment = commentRepository.saveAndFlush(new Comment(userDetails.getUsername(), commentRequestDto.getComment(), userDetails.getUser(), schedule ));
        CommentResponseDto commentResponseDto = new CommentResponseDto(schedule.getLocdate(), schedule.getId(), comment.getId(), comment.getComment());
        return new ResponseEntity<>(new Message("댓글 작성 완료",commentResponseDto), HttpStatus.OK);
    }
    //댓글 수정


}
