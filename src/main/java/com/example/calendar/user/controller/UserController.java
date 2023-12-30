package com.example.calendar.user.controller;

import com.example.calendar.common.security.userDetails.UserDetailsImpl;
import com.example.calendar.common.util.Message;
import com.example.calendar.user.dto.FriendRequestDto;
import com.example.calendar.user.dto.UserRequestDto;
import com.example.calendar.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    //회원 정보 변경
    @PatchMapping(value = "/user/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> updateUser(@ModelAttribute UserRequestDto userRequestDto,
                                              @RequestParam Long userId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return userService.updateUser(userId, userRequestDto, userDetails);
    }
    //회원 탈퇴
    @DeleteMapping("/user/delete")
    public ResponseEntity<Message> deleteUser(@RequestParam Long userId, String email, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.deleteUser(userId, email, userDetails);
    }
    //회원 찾기
    @GetMapping("/user/get")
    public ResponseEntity<Message> getUser(@RequestParam String email, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getUser(email, userDetails.getUser());
    }
    //친구 추가
    @PostMapping("user/friend/create")
    public ResponseEntity<Message> createFriend(@RequestBody FriendRequestDto friendRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.createFriend(friendRequestDto, userDetails);
    }
    //친구 요청 수락
    @PatchMapping("/user/friend/update")
    public ResponseEntity<Message> updateFriend(@RequestBody FriendRequestDto friendRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.updateFriend(friendRequestDto, userDetails);
    }
    //친구 목록 조회
    @GetMapping("/user/friend/get")
    public ResponseEntity<Message> getFriend(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getFriend(userDetails.getUser());
    }
}
