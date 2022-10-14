package com.assignment.postblog.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    //권한이 없는 사용자가 해당 페이지를 접속할 떄 발생하는 예외를 서블렛 단계로 보내주기 위해 사용
    //해당 URI의 controller로 전달
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        response.sendRedirect("/exception/access");
    }
}
