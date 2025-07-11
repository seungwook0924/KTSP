package com.seungwook.ktsp.global.auth.config;

import com.seungwook.ktsp.global.auth.filter.IpBanFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final IpBanFilter ipBanFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception
    {
        return http
                .cors(Customizer.withDefaults()) // CORS 활성화 (WebMvcConfigurer의 설정 사용)
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/service/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin/**", "/auth/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint) // 인증 실패
                        .accessDeniedHandler(accessDeniedHandler) // 권한 부족
                )
                .httpBasic(AbstractHttpConfigurer::disable) // httpBasic 제거 또는 비활성화
                .addFilterBefore(ipBanFilter, UsernamePasswordAuthenticationFilter.class) // IP 필터
                .build();
    }

    /**
     * AuthenticationManager Bean 제공
     * UserDetailsService 없이도 경고 메시지 해결
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) {
        return http.getSharedObject(AuthenticationManager.class);
    }
}
