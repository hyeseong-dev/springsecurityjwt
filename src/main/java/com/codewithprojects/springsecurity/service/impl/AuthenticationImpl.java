package com.codewithprojects.springsecurity.service.impl;

import com.codewithprojects.springsecurity.dto.JwtAuthenticationResponse;
import com.codewithprojects.springsecurity.dto.RefreshTokenRequest;
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

    /**
     * signin 메서드는 사용자 로그인 과정을 처리합니다.
     * 이 메서드는 SigninRequest 객체를 받아 사용자 인증을 수행하고, 성공적으로 인증된 경우 JWT 토큰을 생성합니다.
     *
     * @param signinRequest 사용자 로그인을 위해 필요한 정보를 담은 SigninRequest 객체.
     *                      이 객체에는 사용자의 이메일과 비밀번호가 포함됩니다.
     * @return JwtAuthenticationResponse 객체. 이 객체는 생성된 액세스 토큰과 리프레시 토큰을 포함합니다.
     * @throws UsernameNotFoundException 사용자 이메일이 데이터베이스에 존재하지 않을 경우 발생합니다.
     * @throws IllegalArgumentException 잘못된 이메일 또는 비밀번호 입력 시 발생합니다.
     */
    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
        // 사용자 인증을 위해 AuthenticationManager를 사용합니다.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword())
        );

        // 사용자 이메일을 기반으로 사용자 정보를 조회합니다.
        User userEntity = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        // JWT 액세스 토큰 및 리프레시 토큰을 생성합니다.
        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), userEntity);

        // 생성된 토큰을 응답 객체에 담아 반환합니다.
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setAccessToken(accessToken);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getRefreshToken());
        User userEntity = userRepository.findByEmail(userEmail).orElseThrow();

        if(jwtService.isTokenValid(refreshTokenRequest.getRefreshToken(), userEntity)){
            String accessToken = jwtService.generateAccessToken(userEntity);

            // 생성된 토큰을 응답 객체에 담아 반환합니다.
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setAccessToken(accessToken);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getRefreshToken());
            return jwtAuthenticationResponse;
        }
        return null;
    }
}
