package com.assignment.postblog.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenExceptionController {
//    @GetMapping("/exception/entrypoint")
//    public void entryPoint() {
//        throw new CustomException(ErrorCode.NO_LOGIN);
//    }
//    @GetMapping("/exception/access")
//    public void denied() {
//        throw new CustomException(ErrorCode.NO_ADMIN);
//    }
    // entrypoint와 deniedhandler에서 전달받은 예외들을 처리해주는 controller
    // CustomException 을 만들어서 정해진 형식으로 예외를 반환
}
