package com.example.calendar.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "INTERNAL_SERVER_ERROR"),
    IMAGE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"500",  "s3 이미지 업로드 실패."),
    IMAGE_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"500",  "s3 이미지 삭제 실패."),
    ALREADY_SENT_FRIEND(HttpStatus.INTERNAL_SERVER_ERROR,"500",  "이미 친구요청을 보냈습니다"),

    // 400
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "400" , "토큰이 유효하지 않습니다."),
    NONEXISTENT_COMMENT(HttpStatus.BAD_REQUEST, "400", "해당 댓글이 존재하지 않습니다."),
    INVALID_TYPE(HttpStatus.BAD_REQUEST, "400", "type을 확인해주세요."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED,  "400", "Expired JWT token, 만료된 JWT token 입니다."),

    // 401
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "401", "로그인이 필요합니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "401", "비밀번호가 일치하지 않습니다."),

    // 403
    FORBIDDEN_MEMBER(HttpStatus.FORBIDDEN,"403", "권한이 없는 사용자입니다."),
    FORBIDDEN_MANAGER(HttpStatus.FORBIDDEN, "403", "관리자 권한이 없습니다."),


    //404 NOT_FOUND,
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"404", "유저를 찾을 수 없습니다."),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "존재하지 않는 방 입니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "존재하지 않는 스케줄입니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"404" ,"존재하지 않는 댓글입니다." ),
    FRIEND_NOT_FOUND(HttpStatus.NOT_FOUND,"404" ,"존재하지 않는 친구입니다." ),
    FRIEND_LIST_NOT_FOUND(HttpStatus.NOT_FOUND,"404" ,"친구가 존재하지 않습니다." ),
    FRIEND_PERMISSON_NOT_FOUND(HttpStatus.NOT_FOUND,"404" ,"존재하지 않는 친구 요청 입니다." );

    private final HttpStatus status;
    private final String code;
    private final String message;
}
