package com.zeroone.kstp.config;

import com.zeroone.kstp.service.details.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(CustomUserDetailsService userDetailsService, CustomAuthenticationSuccessHandler successHandler) {
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
    }

    // PasswordEncoder 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Initializing PasswordEncoder...");
        return new BCryptPasswordEncoder();
    }

    // DaoAuthenticationProvider 등록
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        log.info("Initializing DaoAuthenticationProvider...");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // AuthenticationManager 설정
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("Initializing AuthenticationManager...");
        return config.getAuthenticationManager();
    }

    // SecurityFilterChain 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Initializing SecurityFilterChain...");
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",                      // 홈 페이지
                                "/api/email/**",         //이메일 인증
                                "/login/**",             // 로그인 관련 경로
                                "/logout",             // 로그아웃
                                "/register/**",          // 회원가입 관련 경로
                                "/static/**",            // 정적 리소스
                                "/css/**", "/js/**", "/images/**",
                                "/error",                // 에러 페이지
                                "/health"                // 헬스 체크
                        ).permitAll() // 인증 없이 접근 가능
                        .anyRequest().authenticated() // 그 외는 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/login") // 커스텀 로그인 페이지
                        .usernameParameter("studentNumber") // username 대신 studentNumber 매핑
                        .passwordParameter("password")      // password 필드 매핑
                        .successHandler(successHandler) // 성공 핸들러 설정
                        .failureUrl("/login?error=true") // 로그인 실패 시 리다이렉트
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 경로
                        .logoutSuccessUrl("/login") // 로그아웃 성공 시 리다이렉트
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                );

        return http.build();
    }
}