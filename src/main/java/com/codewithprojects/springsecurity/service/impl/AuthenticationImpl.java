package com.codewithprojects.springsecurity.service.impl;

import com.codewithprojects.springsecurity.dto.JwtAuthenticationResponse;
import com.codewithprojects.springsecurity.dto.SignUpRequest;
import com.codewithprojects.springsecurity.dto.SigninRequest;
import com.codewithprojects.springsecurity.entities.Role;
import com.codewithprojects.springsecurity.entities.User;
import com.codewithprojects.springsecurity.repository.UserRepository;
import com.codewithprojects.springsecurity.service.AuthenticationService;
import com.codewithprojects.springsecurity.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * AuthenticationImpl 클래스는 AuthenticationService 인터페이스를 구현합니다.
 * 이 클래스는 인증 관련 데이터를 관리하고, 인증 정보를 제공하는 서비스를 구현합니다.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    /**
     * signup 메서드는 사용자 등록 과정을 처리합니다.
     * 이 메서드는 SignUpRequest 객체를 받아 새로운 사용자(User)를 생성하고 저장합니다.
     *
     * @param signUpRequest 사용자 등록을 위해 필요한 정보를 담은 SignUpRequest 객체.
     *                      이 객체에는 사용자의 이메일, 이름, 성, 비밀번호 등이 포함됩니다.
     * @return 저장된 User 객체. 이 객체는 등록된 사용자의 정보를 포함하며, 저장 후 반환됩니다.
     *         User 객체는 사용자의 이메일, 이름, 성, 역할(Role.USER) 및 암호화된 비밀번호를 포함합니다.
     */
    public User signup(SignUpRequest signUpRequest){
        User userEntity = new User();
        userEntity.setEmail(signUpRequest.getEmail());
        userEntity.setFirstname(signUpRequest.getFirstname());
        userEntity.setSecondname(signUpRequest.getLastname());
        userEntity.setRole(Role.USER);
        userEntity.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        return userRepository.save(userEntity);
    }

    public JwtAuthenticationResponse signin(SigninRequest signinRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),
                        signinRequest.getPassword()));
        User userEntity = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), userEntity);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setAccessToken(accessToken);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }
}
