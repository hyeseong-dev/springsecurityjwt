package com.codewithprojects.springsecurity.controller;

import com.codewithprojects.springsecurity.dto.JwtAuthenticationResponse;
import com.codewithprojects.springsecurity.dto.RefreshTokenRequest;
import com.codewithprojects.springsecurity.dto.SignUpRequest;
import com.codewithprojects.springsecurity.dto.SigninRequest;
import com.codewithprojects.springsecurity.entities.User;
import com.codewithprojects.springsecurity.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * 사용자 등록 요청을 처리하는 엔드포인트입니다.
     * HTTP POST 요청을 "/api/v1/auth/signup" 경로로 받아 사용자 등록을 수행합니다.
     *
     * @param signUpRequest 사용자 등록을 위한 정보가 담긴 SignUpRequest 객체.
     *                      JSON 형식의 요청 본문에서 받은 데이터를 이용하여 생성됩니다.
     *                      이 객체에는 사용자의 이메일, 이름, 성, 비밀번호 등이 포함됩니다.
     * @return ResponseEntity 객체를 통해 HttpStatus.OK(200) 상태 코드와 함께
     *         등록된 사용자 정보(User 객체)를 반환합니다.
     */
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authenticationService.signup(signUpRequest));
    }

    /**
     * 사용자 로그인 요청을 처리하는 엔드포인트입니다.
     * HTTP POST 요청을 "/api/v1/auth/signin" 경로로 받아 사용자 로그인을 수행합니다.
     *
     * @param signinRequest 사용자 로그인을 위한 정보가 담긴 SigninRequest 객체.
     *                      JSON 형식의 요청 본문에서 받은 데이터를 이용하여 생성됩니다.
     *                      이 객체에는 사용자의 이메일과 비밀번호가 포함됩니다.
     * @return ResponseEntity 객체를 통해 HttpStatus.OK(200) 상태 코드와 함께
     *         JWT 인증 응답(JwtAuthenticationResponse) 객체를 반환합니다.
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest signinRequest){
        return ResponseEntity.ok(authenticationService.signin(signinRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }
}
