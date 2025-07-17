package com.seungwook.ktsp.global.auth.config;

import com.seungwook.ktsp.domain.user.repository.UserRepository;
import com.seungwook.ktsp.global.auth.filter.RememberMeAuthenticationFilter;
import com.seungwook.ktsp.global.auth.service.RememberMeTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.List;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록
@EnableMethodSecurity // @PreAuthorize 사용을 위해
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final RememberMeTokenService rememberMeTokenService;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http, SessionRegistry registry) throws Exception
    {
        // 현재 구조는 SecurityContext를 직접 만드는 구조이기 때문에 maximumSessions, maxSessionsPreventsLogin는 실제 작동하지 않음
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // httpBasic 제거 또는 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // form 로그인 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 필요할 때만 세션 생성
                        .maximumSessions(1) // 동시 세션 1개 제한
                        .maxSessionsPreventsLogin(false) // 새 로그인이 오면 기존 세션 강제 만료
                        .sessionRegistry(registry) // SessionRegistry 연동
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**", "/account/**", "/verify/**",
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**").permitAll()
                        .requestMatchers("/service/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new RememberMeAuthenticationFilter(rememberMeTokenService, userRepository, registry), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint) // 인증 실패
                        .accessDeniedHandler(accessDeniedHandler) // 권한 부족
                )
                .build();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        String active = System.getProperty("spring.profiles.active");

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        if ("prod".equals(active)) {
            config.setAllowedOriginPatterns(List.of("https://ktsp.seungwook.com", "http://localhost:3000"));
        } else {
            config.setAllowedOriginPatterns(List.of("http://localhost:3000", "http://localhost:5173"));
        }

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("Content-Type", "X-Forwarded-For"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // 로그인 사용자 세션 추적을 위한 레지스트리 빈 등록
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // UserDetailsService 없이도 경고 메시지 해결
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // bcrypt 인코더
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
