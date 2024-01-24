package com.codewithprojects.springsecurity.service.impl;

import com.codewithprojects.springsecurity.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * JWTServiceImpl 클래스는 JWT 처리를 위한 서비스를 구현합니다.
 * 이 클래스는 JWT의 생성, 추출, 유효성 검증 등의 기능을 제공합니다.
 */
@Service
public class JWTServiceImpl implements JWTService {

    /**
     * 주어진 사용자의 상세 정보를 바탕으로 JWT 액세스 토큰을 생성합니다.
     * 토큰에는 사용자 이름과 발행 시간, 만료 시간이 포함됩니다.
     * 기본 만료 시간은 현재 시간으로부터 24시간입니다.
     *
     * @param userDetails 사용자의 상세 정보
     * @return 생성된 JWT 액세스 토큰
     */
    public String generateAccessToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1000(1ms), 1000 * 60(1초), 1000 * 60 * 60(1시간) * 1000 * 60 * 60 * 24(하루)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 주어진 사용자 상세 정보와 추가 클레임을 포함하는 JWT 리프레시 토큰을 생성합니다.
     * 이 토큰은 일반적으로 장기적 유효성을 가지며, 액세스 토큰 재발급에 사용됩니다.
     *
     * @param extraClaims 추가적인 클레임 정보
     * @param userDetails 사용자의 상세 정보
     * @return 생성된 JWT 리프레시 토큰
     */
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1000(1ms), 1000 * 60(1초), 1000 * 60 * 60(1시간) * 1000 * 60 * 60 * 24(하루)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT 토큰에서 사용자 이름(Username)을 추출합니다.
     *
     * @param token 분석할 JWT 토큰
     * @return 토큰에서 추출된 사용자 이름
     */
    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * JWT 토큰 서명에 사용될 키를 생성합니다.
     * 이 키는 암호화된 문자열로부터 생성되며, 토큰의 무결성 및 보안을 보장하는 데 사용됩니다.
     *
     * @return JWT 서명에 사용될 키
     */
    private Key getSigningKey(){
        byte[] key = Decoders.BASE64.decode("superstrongsecret");
        return Keys.hmacShaKeyFor(key);
    }

    /**
     * JWT 토큰에서 주어진 클레임 resolver 함수를 사용하여 특정 정보를 추출합니다.
     *
     * @param token JWT 토큰
     * @param claimsResolvers 클레임을 추출하기 위한 함수
     * @param <T> 추출될 정보의 타입
     * @return 추출된 정보
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers){
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * JWT 토큰에서 모든 클레임 정보를 추출합니다.
     * 이 정보는 토큰의 유효성 검증, 사용자 인증 등에 사용됩니다.
     *
     * @param token JWT 토큰
     * @return 추출된 모든 클레임 정보
     */
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * 주어진 JWT 토큰이 유효한지 확인합니다.
     * 이 메서드는 토큰이 만료되었는지, 그리고 사용자 상세 정보와 일치하는지 확인합니다.
     *
     * @param token 검사할 JWT 토큰
     * @param userDetails 검증에 사용할 사용자의 상세 정보
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * JWT 토큰이 만료되었는지 확인합니다.
     *
     * @param token 검사할 JWT 토큰
     * @return 토큰이 만료되었으면 true, 아니면 false
     */
    private boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

}
