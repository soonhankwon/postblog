package com.assignment.postblog.jwt;

import com.assignment.postblog.dto.TokenDto;
import com.assignment.postblog.model.RefreshToken;
import com.assignment.postblog.repository.RefreshTokenRepository;
import com.assignment.postblog.security.MemberDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    // 토큰 생성 , header 토큰을 가져오는 기능, 토큰 검증, 리프레쉬 토큰 검증, 인증 객체 생성, 토큰에서 닉네임을 가져오는 기능
    private final MemberDetailsServiceImpl memberDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            //30분
    private static final long REFRESH_TOKEN_EXPRIRE_TIME = 1000 * 60 * 60 * 24 * 7;     //7일
    public static final String ACCESS_TOKEN = "Access_Token";
    public static final String REFRESH_TOKEN = "Refresh_Token";
    //토큰 유효시간 설정
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }
    // header 토큰을 가져오는 기능
    public String getHeaderToken (HttpServletRequest request, String type) {
        return type.equals("Access") ? request.getHeader(ACCESS_TOKEN) :request.getHeader(REFRESH_TOKEN);
    }
    // 토큰 생성
    public TokenDto createAllToken(String nickname) {
        return new TokenDto(createToken(nickname, "Access"), createToken(nickname, "Refresh"));
    }
    // 토큰 생성
    public String createToken(String nickname, String type) {
        Date date = new Date();
        long time = type.equals("Access") ? ACCESS_TOKEN_EXPIRE_TIME : REFRESH_TOKEN_EXPRIRE_TIME;

        return Jwts.builder()
                .setSubject(nickname)
                .setExpiration(new Date(date.getTime() + time))
                .setIssuedAt(date)
                .signWith(key,signatureAlgorithm)
                .compact();
    }
    // 토큰 검증, parseBuilder() 토큰을 분해하는 코드
    public Boolean tokenValidation(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }
    // refressh 토큰 검증
    public Boolean refreshTokenValidation(String token) {
        //1차 토큰 검증
        if(!tokenValidation(token)) return false;

        //DB에 저장한 토큰 비교
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberNickname(getNicknameFromToken(token));

        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String nickname) {
        UserDetails userDetails = memberDetailsService.loadUserByUsername(nickname);
        return new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
    }

    // 토큰에서 nickname을 가져오는 기능
    public String getNicknameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}

//    public JwtTokenProvider(@Value("${jwt.token.key}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
//        this.refreshTokenRepository = refreshTokenRepository;
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public TokenDto generateTokenDto(Member member) {
//        long now = (new Date().getTime());
//
//        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
//        String accessToken = Jwts.builder()
//                .setSubject(member.getNickname())
//                .claim(AUTHORITIES_KEY, MemberRoleEnum.ROLE_MEMBER.toString())
//                .setExpiration(accessTokenExpiresIn)
//                .signWith(SignatureAlgorithm.HS256, key)
//                .compact();
//
//        String refreshToken = Jwts.builder()
//                .setExpiration(new Date(now + REFRESH_TOKEN_EXPRIRE_TIME))
//                .signWith(SignatureAlgorithm.HS256, key)
//                .compact();
//
//        RefreshToken refreshTokenObject = RefreshToken.builder()
//                .id(member.getId())
//                .member(member)
//                .value(refreshToken)
//                .build();
//
//        refreshTokenRepository.save(refreshTokenObject);
//
//        return TokenDto.builder()
//                .grantType(BEARER_PREFIX)
//                .accessToken(accessToken)
//                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
//                .refreshToken(refreshToken)
//                .build();
//    }
//    public Member getMemberFromAuthentication() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || AnonymousAuthenticationToken.class.
//                isAssignableFrom(authentication.getClass())) {
//            return null;
//        }
//        return ((MemberDetailsImpl) authentication.getPrincipal()).getMember();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser()
//                    .setSigningKey(key)
//                    .parseClaimsJws(token);
//            return true;
//        } catch (SecurityException | MalformedJwtException e) {
//            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
//        } catch (ExpiredJwtException e) {
//            log.info("Expired JWT token, 만료된 JWT token 입니다.");
//        } catch (UnsupportedJwtException e) {
//            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
//        } catch (IllegalArgumentException e) {
//            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
//        }
//        return false;
//    }
//    @Transactional(readOnly = true)
//    public RefreshToken isPresentRefreshToken(Member member) {
//        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByMember(member);
//        return optionalRefreshToken.orElse(null);
//    }
//
//    @Transactional
//    public ResponseDto<?> deleteRefreshToken(Member member) {
//        RefreshToken refreshToken = isPresentRefreshToken(member);
//        if (null == refreshToken) {
//            return ResponseDto.fail("TOKEN_NOT_FOUND", "존재하지 않는 Token 입니다.");
//        }
//
//        refreshTokenRepository.delete(refreshToken);
//        return ResponseDto.success("success");
//    }
