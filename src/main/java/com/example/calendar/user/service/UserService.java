package com.example.calendar.user.service;

import com.example.calendar.common.S3.S3Uploader;
import com.example.calendar.common.exception.CustomException;
import com.example.calendar.common.exception.ErrorCode;
import com.example.calendar.common.security.jwt.JwtUtil;
import com.example.calendar.common.security.userDetails.UserDetailsImpl;
import com.example.calendar.common.util.Message;
import com.example.calendar.user.dto.UserRequestDto;
import com.example.calendar.user.dto.UserResponseDto;
import com.example.calendar.user.entity.User;
import com.example.calendar.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;
    private final JwtUtil jwtUtil;

    //회원 정보 변경
    @Transactional
    public ResponseEntity<Message> updateUser(Long userId, UserRequestDto userRequestDto, UserDetailsImpl userDetails) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        if (user.getId() == userDetails.getUser().getId()) {
            if(userRequestDto.getNewProfileImage() == null) {
                user.update(userRequestDto.getNickName(), userRequestDto.getProfileImage());
                userRepository.saveAndFlush(user);
            } else {
                if(user.getProfileImage() == "https://kim-or-jang-calendar-profile.s3.ap-northeast-2.amazonaws.com/user.png"){
                    String profile = s3Uploader.upload(userRequestDto.getNewProfileImage());
                    user.update(userRequestDto.getNickName(), profile);
                    userRepository.saveAndFlush(user);
                }else{
                    s3Uploader.delete(user.getProfileImage());
                    String profile = s3Uploader.upload(userRequestDto.getNewProfileImage());
                    user.update(userRequestDto.getNickName(), profile);
                    userRepository.saveAndFlush(user);
                }
            }
            UserResponseDto userResponseDto = new UserResponseDto(user.getEmail(), user.getProfileImage(), user.getNickName());
            return new ResponseEntity<>(new Message("개인정보가 수정 되었습니다.",userResponseDto), HttpStatus.OK);
        } else {
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER);
        }
    }
    //회원 탈퇴
    public ResponseEntity<Message> deleteUser(Long userId, String email, UserDetailsImpl userDetails) {
        User user = userRepository.findByIdAndEmail(userId, email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        if(user.getId() == userDetails.getUser().getId() || user.getEmail() == userDetails.getUser().getEmail()){
            userRepository.deleteByIdAndEmail(userId, email);
        }
        return new ResponseEntity<>(new Message("회원삭제 성공", user), HttpStatus.OK);
    }
    //회원 찾기
    public ResponseEntity<Message> getUser(String email, User user) {
        List<UserResponseDto> userResponseDtoList = userRepository.findAllByEmail(email);
        return new ResponseEntity<>(new Message(null,userResponseDtoList),HttpStatus.OK);
    }
}
