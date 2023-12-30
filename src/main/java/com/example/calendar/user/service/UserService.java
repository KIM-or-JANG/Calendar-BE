package com.example.calendar.user.service;

import com.example.calendar.common.S3.S3Uploader;
import com.example.calendar.common.exception.CustomException;
import com.example.calendar.common.exception.ErrorCode;
import com.example.calendar.common.security.jwt.JwtUtil;
import com.example.calendar.common.security.userDetails.UserDetailsImpl;
import com.example.calendar.common.util.Message;
import com.example.calendar.user.dto.FriendRequestDto;
import com.example.calendar.user.dto.FriendResponseDto;
import com.example.calendar.user.dto.UserRequestDto;
import com.example.calendar.user.dto.UserResponseDto;
import com.example.calendar.user.entity.Friend;
import com.example.calendar.user.entity.User;
import com.example.calendar.user.repository.FriendRepository;
import com.example.calendar.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final FriendRepository friendRepository;
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
    @Transactional
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
    //친구 추가 요청
    public ResponseEntity<Message> createFriend(FriendRequestDto friendRequestDto, UserDetailsImpl userDetails) {
        User fromUser = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new CustomException(ErrorCode.FORBIDDEN_MEMBER)
        );
        User toUser = userRepository.findByEmailAndNickName(friendRequestDto.getEmail(),friendRequestDto.getNickName()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        friendRepository.findByFromUserIDAndToUserID(fromUser, toUser).ifPresent(
                value -> {
                    throw new CustomException(ErrorCode.ALREADY_SENT_FRIEND);
                });
        Friend friend = new Friend(fromUser, toUser);
        friendRepository.saveAndFlush(friend);
        FriendResponseDto friendResponseDto = new FriendResponseDto(fromUser, toUser);
        return new ResponseEntity<>(new Message("친구 요청 성공", friendResponseDto), HttpStatus.OK);
    }
    //친구요청 수락
    public ResponseEntity<Message> updateFriend(boolean permisson, FriendRequestDto friendRequestDto, UserDetailsImpl userDetails) {
        User toUser = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new CustomException(ErrorCode.FORBIDDEN_MEMBER)
        );
        User fromUser = userRepository.findByEmailAndNickName(friendRequestDto.getEmail(),friendRequestDto.getNickName()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        Friend friend = friendRepository.findByFromUserIDAndToUserID(toUser, fromUser).orElseThrow(
                () -> new CustomException(ErrorCode.FRIEND_PERMISSON_NOT_FOUND)
        );
        if(permisson) {
            friend.update(permisson);
            friendRepository.saveAndFlush(friend);
            FriendResponseDto friendResponseDto = new FriendResponseDto(toUser, fromUser);
            return new ResponseEntity<>(new Message("친구 수락", friendResponseDto), HttpStatus.OK);
        } else {
            friendRepository.delete(friend);
            return new ResponseEntity<>(new Message("친구 거절", null), HttpStatus.OK);
        }
    }
    //친구 목록 조회
    public ResponseEntity<Message> getFriend(User user) {
        userRepository.findById(user.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.FORBIDDEN_MEMBER)
        );
        boolean permission = true;
        List<Friend> friendList = friendRepository.findByFromUserIDORToUserIDAndweAreFriend(user.getId(), permission);
        if(friendList.isEmpty()) {
            return new ResponseEntity<>(new Message("등록된 친구가 없습니다.", null), HttpStatus.OK);
        }
        List<FriendResponseDto> friendResponseDtoList = new ArrayList<>();
        for(Friend friend : friendList){
            if(user.getId() == friend.getFromUserID().getId()) {
                FriendResponseDto friendResponseDto = new FriendResponseDto();
                friendResponseDto.toUser(
                        friend.getToUserID().getEmail(),
                        friend.getToUserID().getNickName(),
                        friend.getToUserID().getProfileImage()
                );
                friendResponseDtoList.add(friendResponseDto);
            } if(user.getId() == friend.getToUserID().getId()) {
                FriendResponseDto friendResponseDto = new FriendResponseDto();
                friendResponseDto.fromUser(
                        friend.getFromUserID().getEmail(),
                        friend.getFromUserID().getNickName(),
                        friend.getFromUserID().getProfileImage()
                );
                friendResponseDtoList.add(friendResponseDto);
            }
        }
        return new ResponseEntity<>(new Message("조회 성공", friendResponseDtoList), HttpStatus.OK);
    }
    //친구 삭제
    public ResponseEntity<Message> deleteUser(FriendRequestDto friendRequestDto, UserDetailsImpl userDetails) {
        User toUser = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new CustomException(ErrorCode.FORBIDDEN_MEMBER)
        );
        User fromUser = userRepository.findByEmailAndNickName(friendRequestDto.getEmail(),friendRequestDto.getNickName()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        Friend friend = friendRepository.findByFromUserIDAndToUserID(toUser, fromUser).orElseThrow(
                () -> new CustomException(ErrorCode.FRIEND_PERMISSON_NOT_FOUND)
        );
        friendRepository.delete(friend);
        return new ResponseEntity<>(new Message("삭제 성공", null), HttpStatus.OK);
    }
}
