package com.example.calendar.scheadule.controller;

import com.example.calendar.common.security.userDetails.UserDetailsImpl;
import com.example.calendar.common.util.Message;
import com.example.calendar.scheadule.dto.CommentRequestDto;
import com.example.calendar.scheadule.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    //댓글 작성
    @PostMapping("/comment/create")
    public ResponseEntity<Message> createComment(@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.createComment(commentRequestDto, userDetails);
    }
    //댓글 수정
    @PatchMapping("/comment/update")
    public ResponseEntity<Message> updateComment(@RequestParam Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentId, commentRequestDto, userDetails);
    }
    //댓글 삭제
    @DeleteMapping("/comment/delete")
    public ResponseEntity<Message> deleteComment(@RequestParam Long scheduleId, Long commentId, Long roomId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.deleteComment(scheduleId, commentId, roomId, userDetails);
    }
}
