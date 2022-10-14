package com.assignment.postblog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ADMIN_TOKEN(HttpStatus.BAD_REQUEST, "관리자 암호가 일치하지 않습니다"),
    SAME_NICKNAME(HttpStatus.BAD_REQUEST, "동일한 닉네임이 존재합니다"),
    NO_USER(HttpStatus.BAD_REQUEST, "없는 사용자입니다"),
    NO_LOGIN(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다"),
    NO_ADMIN(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다");

    private HttpStatus httpStatus;
    private String detail;
}
