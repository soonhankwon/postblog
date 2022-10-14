package com.assignment.postblog.service;

import com.assignment.postblog.dto.GlobalResDto;
import com.assignment.postblog.dto.LoginMemberDto;
import com.assignment.postblog.dto.SignupRequestDto;
import com.assignment.postblog.dto.TokenDto;
import com.assignment.postblog.jwt.JwtTokenProvider;
import com.assignment.postblog.model.Member;
import com.assignment.postblog.model.RefreshToken;
import com.assignment.postblog.repository.MemberRepository;
import com.assignment.postblog.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public GlobalResDto signup(SignupRequestDto signupRequestDto) {
        //nickname 중복검사
        if (memberRepository.findByNickname(signupRequestDto.getNickname()).isPresent()) {
            throw new RuntimeException("Overlap Check");
        }
        signupRequestDto.setEncodePwd(passwordEncoder.encode(signupRequestDto.getPassword()));
        Member member = new Member(signupRequestDto);

        memberRepository.save(member);
        return new GlobalResDto("Success signup", HttpStatus.OK.value());
    }

    @Transactional
    public GlobalResDto login(LoginMemberDto loginMemberDto, HttpServletResponse response) {
        Member member = memberRepository.findByNickname(loginMemberDto.getNickname()).orElseThrow(
                () -> new RuntimeException("Not found Member"));
        if (!passwordEncoder.matches(loginMemberDto.getPassword(), member.getPassword())) {
            throw new RuntimeException("Not matches Password");
        }

        TokenDto tokenDto = jwtTokenProvider.createAllToken(loginMemberDto.getNickname());
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberNickname(loginMemberDto.getNickname());

        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginMemberDto.getNickname());
            refreshTokenRepository.save(newToken);
        }
        setHeader(response, tokenDto);

        return new GlobalResDto("Success Login", HttpStatus.OK.value());
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtTokenProvider.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtTokenProvider.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}
//    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";
//@Autowired
//public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
//    this.memberRepository = memberRepository;
//    this.passwordEncoder = passwordEncoder;
//}
//    public void registerMember(SignupRequestDto requestDto) {
//        String nickname = requestDto.getNickname();
//        // 회원 ID 중복 확인
//        Optional<Member> found = memberRepository.findByNickname(nickname);
//        if (found.isPresent()) {
//            throw new IllegalArgumentException("중복된 닉네임이 존재합니다");
//        }
//        String password = passwordEncoder.encode(requestDto.getPassword());
//        String passwordConfirm = passwordEncoder.encode(requestDto.getPasswordConfirm());
//        // 사용자 ROLE 확인
//        MemberRoleEnum role = MemberRoleEnum.USER;
//        if (requestDto.isAdmin()) {
//            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
//                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
//            }
//            role = MemberRoleEnum.ADMIN;
//        }
//        Member member = new Member(nickname, password, passwordConfirm, role);
//        memberRepository.save(member);
//    }
//    // 로그인
//    public Member login(LoginMemberDto loginMemberDto) {
//        Member member = memberRepository.findByNickname(loginMemberDto.getNickname()).orElseThrow(
//                () -> new CustomException(ErrorCode.NO_USER));
//        if (!passwordEncoder.matches(loginMemberDto.getPassword(), member.getPassword())) {
//            throw new CustomException(ErrorCode.NO_USER);
//        }
//        return member;
//    }
//}