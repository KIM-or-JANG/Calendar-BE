package com.example.calendar.user.controller;

import com.example.calendar.common.security.userDetails.UserDetailsImpl;
import com.example.calendar.common.util.Message;
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
}
