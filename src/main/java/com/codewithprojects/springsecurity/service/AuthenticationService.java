package com.codewithprojects.springsecurity.service;

import com.codewithprojects.springsecurity.dto.JwtAuthenticationResponse;
import com.codewithprojects.springsecurity.dto.RefreshTokenRequest;
import com.codewithprojects.springsecurity.dto.SignUpRequest;
import com.codewithprojects.springsecurity.dto.SigninRequest;
import com.codewithprojects.springsecurity.entities.User;

/**
 * AuthenticationService 인터페이스는 사용자 인증 관련 서비스를 정의합니다.
 * 이 인터페이스는 사용자의 회원가입(signup) 및 로그인(signin) 등 인증 관련 기능을 명세합니다.
 */
public interface AuthenticationService {

    /**
     * 사용자의 회원가입 요청을 처리합니다.
     * SignUpRequest 객체를 받아 새로운 사용자를 생성하고 데이터베이스에 저장합니다.
     *
     * @param signUpRequest 사용자 등록을 위한 정보가 담긴 SignUpRequest 객체.
     *                      이 객체에는 사용자의 이메일, 이름, 성, 비밀번호 등이 포함됩니다.
     * @return 등록된 새로운 User 객체. 이 객체는 사용자의 이메일, 이름, 성, 역할(Role.USER),
     *         및 암호화된 비밀번호를 포함합니다.
     */
    User signup(SignUpRequest signUpRequest);

    /**
     * 사용자의 로그인 요청을 처리합니다.
     * SigninRequest 객체를 받아 사용자 인증을 수행하고, JWT 인증 응답을 반환합니다.
     *
     * @param signinRequest 사용자 로그인을 위한 정보가 담긴 SigninRequest 객체.
     *                      이 객체에는 사용자의 이메일과 비밀번호가 포함됩니다.
     * @return 성공적인 인증 후 생성된 JWT 인증 응답(JwtAuthenticationResponse) 객체.
     */
    JwtAuthenticationResponse signin(SigninRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}