package com.example.calendar.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
    IMAGE_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "s3 이미지 삭제 실패."),

    // 400
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
    NONEXISTENT_COMMENT(HttpStatus.BAD_REQUEST, "해당 댓글이 존재하지 않습니다."),
    INVALID_TYPE(HttpStatus.BAD_REQUEST, "type을 확인해주세요."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED,  "Expired JWT token, 만료된 JWT token 입니다."),

    // 401
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    // 403
    FORBIDDEN_MEMBER(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),
    FORBIDDEN_ADMIN(HttpStatus.FORBIDDEN,"관리자 권한이 없습니다."),


    //404 NOT_FOUND,
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
