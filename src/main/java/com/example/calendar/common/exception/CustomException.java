package com.example.calendar.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException(ErrorCode e) {
        super(e.getMessage());
        this.errorCode = e;
    }
}
