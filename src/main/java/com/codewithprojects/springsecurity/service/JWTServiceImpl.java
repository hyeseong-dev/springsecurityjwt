package com.codewithprojects.springsecurity.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTServiceImpl implements JWTService{

    /**
     * 주어진 사용자의 상세 정보를 바탕으로 JWT 토큰을 생성합니다.
     *
     * @param userDetails 사용자의 상세 정보
     * @return 생성된 JWT 토큰 문자열
     */
    private String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT 토큰에서 사용자 이름(Username)을 추출합니다.
     *
     * @param token 분석할 JWT 토큰
     * @return 토큰에서 추출된 사용자 이름
     */
    public String ExtractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * JWT 서명에 사용될 키를 생성합니다.
     *
     * @return 생성된 서명 키
     */
    private Key getSigningKey(){
        byte[] key = Decoders.BASE64.decode("superstrongsecret");
        return Keys.hmacShaKeyFor(key);
    }

    /**
     * 주어진 JWT 토큰에서 특정 클레임을 추출합니다.
     *
     * @param token 분석할 JWT 토큰
     * @param claimsResolver 클레임을 추출하기 위한 함수
     * @param <T> 추출될 클레임의 타입
     * @return 추출된 클레임 값
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers){
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * JWT 토큰에서 모든 클레임을 추출합니다.
     *
     * @param token 분석할 JWT 토큰
     * @return 토큰에서 추출된 모든 클레임
     */
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

}
