package com.codewithprojects.springsecurity.service.impl;

import com.codewithprojects.springsecurity.repository.UserRepository;
import com.codewithprojects.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl 클래스는 UserService 인터페이스를 구현합니다.
 * 이 클래스는 사용자 관련 데이터를 관리하고, 사용자 상세 정보를 제공하는 서비스를 구현합니다.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Spring Security의 UserDetailsService를 제공합니다.
     * 이 메서드는 사용자 이름(이메일)을 기반으로 UserDetails 객체를 로드하는 기능을 제공합니다.
     *
     * @return UserDetailsService 인스턴스
     */
    @Override
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            /**
             * 주어진 사용자 이름(이메일)으로 UserDetails 객체를 로드합니다.
             * 해당 이메일 주소를 가진 사용자가 없는 경우 UsernameNotFoundException을 발생시킵니다.
             *
             * @param username 사용자의 이메일 주소
             * @return 로드된 UserDetails 객체
             * @throws UsernameNotFoundException 사용자가 존재하지 않을 때 발생하는 예외
             */
            @Override
            public UserDetails loadUserByUsername(String username){
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }
}
