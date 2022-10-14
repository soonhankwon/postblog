package com.assignment.postblog.jwt;

import com.assignment.postblog.dto.GlobalResDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
//시큐리티 필터가 돌기전 토큰을 인증해주는 필터
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    public static String AUTHORIZATION_HEADER = "Authorization";
    public static String BEARER_PREFIX = "Bearer ";
    public static String AUTHORITIES_KEY = "auth";


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String accessToken = jwtTokenProvider.getHeaderToken(request, "Access");
        String refreshToken = jwtTokenProvider.getHeaderToken(request, "Refresh");
        // 클라이언트의 토큰을 가져옴

        if (accessToken != null) {
            if (!jwtTokenProvider.tokenValidation(accessToken)) {
                jwtExeptionHander(response, "AccessToken Expired", HttpStatus.BAD_REQUEST);
                return;
            }
            setAuthentication(jwtTokenProvider.getNicknameFromToken(accessToken));
        } else if(refreshToken != null) {
            if(!jwtTokenProvider.refreshTokenValidation(refreshToken)) {
                jwtExeptionHander(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
                return;
            }
            setAuthentication(jwtTokenProvider.getNicknameFromToken(refreshToken));
        }
        filterChain.doFilter(request, response);
    }
    // 인증 객체 메서드 & 인증 객체 만듬
    public void setAuthentication(String nickname) {
        Authentication authentication = jwtTokenProvider.createAuthentication(nickname);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void jwtExeptionHander(HttpServletResponse response, String msg, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new GlobalResDto(msg, status.value()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
