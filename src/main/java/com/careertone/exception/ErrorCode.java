package com.careertone.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    USER_ALREADY_EXISTS(CONFLICT, "USER_ALREADY_EXISTS", "이미 가입된 사용자입니다."),
    INVALID_CREDENTIALS(UNAUTHORIZED, "INVALID_CREDENTIALS", "존재하지 않는 계정입니다."),
    ACCESS_DENIED(FORBIDDEN, "ACCESS_DENIED", "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."),
    NOT_FOUND_USER(NOT_FOUND, "NOT_FOUND_USER", "해당 id의 유저가 존재하지 않습니다."),
    INVALID_TOKEN(UNAUTHORIZED, "INVALID_TOKEN", "유효하지 않은 인증 토큰입니다."),


    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
