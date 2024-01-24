package com.codewithprojects.springsecurity.controller;

import com.codewithprojects.springsecurity.dto.SignUpRequest;
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
     * signup 메서드는 사용자 등록 요청을 처리하는 엔드포인트입니다.
     * HTTP POST 요청을 "/api/v1/auth/signup" 경로로 받아 사용자 등록을 수행합니다.
     *
     * @param signUpRequest 사용자 등록을 위해 필요한 정보를 담은 SignUpRequest 객체.
     *                      JSON 형식의 요청 본문에서 받은 데이터를 이용하여 생성됩니다.
     *                      이 객체에는 사용자의 이메일, 이름, 성, 비밀번호 등이 포함됩니다.
     * @return ResponseEntity 객체를 통해 HttpStatus.OK(200) 상태 코드와 함께
     *         등록된 사용자 정보(User 객체)를 반환합니다.
     *         User 객체는 사용자의 이메일, 이름, 성, 역할 및 암호화된 비밀번호를 포함합니다.
     */
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authenticationService.signup(signUpRequest));
    }
}
