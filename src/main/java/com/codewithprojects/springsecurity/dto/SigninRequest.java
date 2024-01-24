package com.codewithprojects.springsecurity.dto;

import lombok.Data;

/**
 * SigninRequest 클래스는 사용자의 회원가입 요청 데이터를 전달하는데 사용됩니다.
 * 이 클래스는 사용자의 이메일, 비밀번호 정보를 포함합니다.
 *
 */
@Data
public class SigninRequest {
    private String email;     // 사용자의 이메일 주소
    private String password;  // 사용자의 비밀번호
}
