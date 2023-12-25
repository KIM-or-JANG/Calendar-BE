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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
        private final RoomRepository roomRepository;
        private final RoomUserRepository roomUserRepository;
        private final ScheduleRepository scheduleRepository;
        private final CommentRepository commentRepository;
    //댓글 작성
    @Transactional
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
        CommentResponseDto commentResponseDto = new CommentResponseDto(schedule.getLocdate(), schedule.getId(), schedule.getSchedule(), comment.getId(), comment.getComment(), comment.getUserName());
        return new ResponseEntity<>(new Message("댓글 작성 완료",commentResponseDto), HttpStatus.OK);
    }
    //댓글 수정
    @Transactional
    public ResponseEntity<Message> updateComment(Long commentId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        Room room = roomRepository.findById(commentRequestDto.getRoomId()).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        Schedule schedule = scheduleRepository.findById(commentRequestDto.getScheduleId()).orElseThrow(
                () -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND)
        );
        Comment comment = commentRepository.findByIdAndSchedule_IdAndUser_Id(commentId, commentRequestDto.getScheduleId(), userDetails.getUser().getId()).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)
        );
        if (comment.getUser().getId() == userDetails.getUser().getId() || room.getManager().getId() == userDetails.getUser().getId()) {
            comment.update(commentRequestDto.getComment());
        commentRepository.saveAndFlush(comment);
        } else{
          new CustomException(ErrorCode.FORBIDDEN_MEMBER);
        }
        CommentResponseDto commentResponseDto = new CommentResponseDto(schedule.getLocdate(), schedule.getId(), schedule.getSchedule(), comment.getId(), comment.getComment(), comment.getUserName());
        return new ResponseEntity<>(new Message("댓글 수정 완료", commentResponseDto), HttpStatus.OK);
    }
    //댓글 삭제
    @Transactional
    public ResponseEntity<Message> deleteComment(Long scheduleId, Long commentId, Long roomId, UserDetailsImpl userDetails) {
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND)
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)
        );
        if (comment.getUser().getId() == userDetails.getUser().getId() || room.getManager().getId() == userDetails.getUser().getId()) {
            commentRepository.delete(comment);
        } else {
            new CustomException(ErrorCode.FORBIDDEN_MEMBER);
        }
        CommentResponseDto commentResponseDto = new CommentResponseDto(schedule.getLocdate(), schedule.getId(), schedule.getSchedule(), comment.getId(), comment.getComment(), comment.getUserName());
        return new ResponseEntity<>(new Message("댓글 삭제 완료",commentResponseDto),HttpStatus.OK);
    }
}
