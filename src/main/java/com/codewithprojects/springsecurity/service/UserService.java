package com.codewithprojects.springsecurity.service;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * UserService 인터페이스는 사용자 관련 서비스의 계약을 정의합니다.
 * 이 인터페이스는 사용자 상세 정보를 제공하는 서비스를 위한 메서드를 선언합니다.
 */
public interface UserService {

    /**
     * Spring Security의 UserDetailsService를 제공합니다.
     * UserDetailsService는 사용자 이름(이메일)을 기반으로 UserDetails 객체를 로드하는 기능을 제공합니다.
     *
     * @return UserDetailsService 인스턴스
     */
    UserDetailsService userDetailsService();
}
