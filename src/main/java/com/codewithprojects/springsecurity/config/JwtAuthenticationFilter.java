package com.codewithprojects.springsecurity.config;

import com.codewithprojects.springsecurity.service.JWTService;
import com.codewithprojects.springsecurity.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter는 JWT 기반 인증을 위한 커스텀 필터입니다.
 * 이 필터는 HttpServletRequest에서 "Authorization" 헤더를 통해 전달된 JWT를 추출하고 검증합니다.
 * 유효한 JWT의 경우, 해당 사용자의 인증 정보를 SecurityContext에 설정합니다.
 *
 * 이 클래스는 OncePerRequestFilter를 상속받아 요청당 한 번씩 필터링 로직이 수행되도록 합니다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserService userService;

    /**
     * 요청을 필터링하여 JWT 인증 처리를 수행합니다.
     *
     * @param request HttpServletRequest 객체로, 현재 HTTP 요청 정보를 담고 있습니다.
     * @param response HttpServletResponse 객체로, 현재 HTTP 응답 정보를 담고 있습니다.
     * @param filterChain FilterChain 객체로, 필터 체인을 통해 요청/응답을 다음 단계로 전달합니다.
     * @throws ServletException 요청 처리 중 발생할 수 있는 예외
     * @throws IOException 입출력 처리 중 발생할 수 있는 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // "Authorization" 헤더에서 JWT 토큰을 추출
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // "Authorization" 헤더가 비어있거나 "Bearer "로 시작하지 않는 경우, 필터 체인을 계속 진행
        if(StringUtils.isEmpty(authHeader) || !org.apache.commons.lang3.StringUtils.startsWith(authHeader, "Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " 이후의 문자열을 JWT 토큰으로 추출
        jwt = authHeader.substring(7);
        // JWT 토큰에서 사용자 이메일 추출
        userEmail = jwtService.extractUserName(jwt);

        // 사용자 이메일이 비어있지 않고 현재 SecurityContext에 인증 정보가 없는 경우
        if( (userEmail != null && !userEmail.isEmpty()  )
                && SecurityContextHolder.getContext().getAuthentication() == null){
            // 사용자 상세 정보 로드
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);

            // JWT 토큰이 유효한 경우
            if(jwtService.isTokenValid(jwt, userDetails)){
                // 새로운 SecurityContext 생성
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                // 사용자 인증 토큰 생성 및 설정
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContext에 인증 정보 설정
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            }

        }
        // 다음 필터로 요청과 응답 전달
        filterChain.doFilter(request, response);
        return;
    }
}
