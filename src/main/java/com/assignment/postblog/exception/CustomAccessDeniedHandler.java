package com.assignment.postblog.exception;

import com.assignment.postblog.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                new ObjectMapper().writeValueAsString(
                        ResponseDto.fail("BAD_REQUEST","이 작업은 로그인이 필요합니다.")
                )
        );
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
