package com.assignment.postblog.security;

import com.assignment.postblog.exception.CustomAccessDeniedHandler;
import com.assignment.postblog.exception.CustomAuthenticationEntryPoint;
import com.assignment.postblog.jwt.JwtAuthenticationFilter;
import com.assignment.postblog.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        return (web) -> web.ignoring()
                .antMatchers("/h2-console/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 회원 관리 처리 API (POST /user/**) 에 대해 CSRF 무시
        http.csrf().disable();
//                .ignoringAntMatchers("/post/**")
//                .ignoringAntMatchers("/comment/**")
//                .ignoringAntMatchers("/member/**");
        http
                .authorizeHttpRequests((authz) -> authz
                        // image 폴더를 login 없이 허용
                        .antMatchers("/images/**").permitAll()
                        // css 폴더를 login 없이 허용
                        .antMatchers("/css/**").permitAll()
                        // 회원 관리 처리 API 전부를 login 없이 허용
                        .antMatchers("/member/**").permitAll()
                        // 어떤 요청이든 '인증'
                        .anyRequest().authenticated()
                )
                // 로그인 기능 허용
                .formLogin()
                // 로그인 View 제공 (GET /user/login)
                .loginPage("/member/login")
                // 로그인 처리 (POST /user/login)
                .loginProcessingUrl("/member/login")
                // 로그인 처리 후 성공 시 URL
                .defaultSuccessUrl("/")
                .failureUrl("/member/login?error")
                .permitAll()
                .and()
                //로그아웃 기능 허용
                .logout()
                //로그아웃 처리 URL
                .logoutUrl("/member/logout")
                .permitAll();
        //프론트엔드가 별도로 존재하여 rest Api로 구성한다고 가정
        http.httpBasic().disable(); //스프링시큐리티에서 만들어주는 로그인 페이지를 안쓰기위해
        //세션사용X, JWT 토큰 방식을 사용하면 더이상 세션저장이 필요없으므로 꺼준다.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //Jwt Filter추가, Filter를 사용하겠다고 등록
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        //JwtAuthentication exception handling, 토큰 인증과정에서 발생하는 예외를 처리하기 위한 EntryPoint 등록
        http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        //access Denial handler, 인가에 실패했을 떄(일반 유저가 관리자 페이지로 접속 시도) 예외를 발생시키는 handler등록
        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler());

        return http.build();
    }
}