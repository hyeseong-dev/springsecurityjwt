package com.codewithprojects.springsecurity.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

/**
 * JWTService 인터페이스는 JWT(JSON Web Token) 처리를 위한 서비스를 정의합니다.
 * 이 인터페이스를 구현하는 클래스는 JWT의 생성, 추출 및 유효성 검증을 담당합니다.
 */
public interface JWTService {

    /**
     * 주어진 JWT 토큰에서 사용자 이름(Username)을 추출합니다.
     *
     * @param token 분석할 JWT 토큰
     * @return 토큰에서 추출된 사용자 이름
     */
    String extractUserName(String token);

    /**
     * 사용자의 상세 정보를 기반으로 JWT 토큰을 생성합니다.
     *
     * @param userDetails 사용자의 상세 정보
     * @return 생성된 JWT 토큰
     */
    String generateAccessToken(UserDetails userDetails);

    /**
     * 사용자의 상세 정보를 기반으로 JWT 토큰을 생성합니다.
     *
     * @param userDetails 사용자의 상세 정보
     * @return 생성된 JWT 토큰
     */
    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);

    /**
     * 주어진 JWT 토큰의 유효성을 검사합니다.
     * 이 메서드는 토큰이 유효한지, 그리고 제공된 사용자 상세 정보와 일치하는지 확인합니다.
     *
     * @param token 검사할 JWT 토큰
     * @param userDetails 검증에 사용할 사용자의 상세 정보
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}