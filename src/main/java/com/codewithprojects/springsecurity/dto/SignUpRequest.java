package com.codewithprojects.springsecurity.dto;

import lombok.Data;

/**
 * SignUpRequest 클래스는 사용자의 회원가입 요청 데이터를 전달하는데 사용됩니다.
 * 이 클래스는 사용자의 이름, 성, 이메일, 비밀번호 정보를 포함합니다.
 *
 * @Data 어노테이션은 Lombok 라이브러리를 사용하여 getter, setter, equals, hashCode, toString 메서드를 자동으로 생성합니다.
 */
@Data
public class SignUpRequest {
    private String firstname; // 사용자의 이름
    private String lastname;  // 사용자의 성
    private String email;     // 사용자의 이메일 주소
    private String password;  // 사용자의 비밀번호
}
