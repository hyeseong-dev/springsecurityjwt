package com.codewithprojects.springsecurity.config;

import com.codewithprojects.springsecurity.entities.Role;
import com.codewithprojects.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 스프링 시큐리티를 활용하여 인증 및 권한 부여 설정을 제공하는 구성 클래스입니다.
 * 이 클래스는 JWT를 이용한 인증 체계와 스프링 시큐리티 필터 체인을 구성합니다.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfriguration {
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    /**
     * HTTP 보안을 구성하는 SecurityFilterChain 빈을 생성합니다.
     *
     * @param http HttpSecurity를 이용해 보안 구성을 정의합니다.
     * @return 구성된 SecurityFilterChain 객체입니다.
     * @throws Exception 보안 구성 중 발생하는 예외를 처리합니다.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers("/api/v1/auth/**")
                        .permitAll()
                        .requestMatchers("/api/v1/admin").hasAnyAuthority(Role.Admin.name())
                        .requestMatchers("/api/v1/user").hasAnyAuthority(Role.User.name())
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    /**
     * 사용자 인증을 처리하는 AuthenticationProvider 빈을 생성합니다.
     *
     * @return 구성된 AuthenticationProvider 객체입니다.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
                authenticationProvider.setUserDetailsService(userService.userDetailsService());
                authenticationProvider.setPasswordEncoder(passwordEncoder());
                return authenticationProvider;
    }

    /**
     * 비밀번호 암호화를 위한 PasswordEncoder 빈을 생성합니다.
     *
     * @return 구성된 PasswordEncoder 객체입니다.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager 빈을 생성합니다.
     * 이 메서드는 인증 관리자를 구성합니다.
     *
     * @param config AuthenticationConfiguration을 이용해 인증 관리자를 설정합니다.
     * @return 구성된 AuthenticationManager 객체입니다.
     * @throws Exception 인증 관리자 설정 중 발생하는 예외를 처리합니다.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
