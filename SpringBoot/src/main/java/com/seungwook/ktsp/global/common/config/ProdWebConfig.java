package com.seungwook.ktsp.global.common.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;

@Profile("prod")
@Configuration
public class ProdWebConfig implements WebMvcConfigurer {

    // CORS 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 요청 URL 허용
                .allowedOrigins("http://localhost:3000") // 요청 출처 5173 포트 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // HTTP 허용 메서드
                .allowedHeaders("Content-Type") // 허용 헤더
                .allowCredentials(true); // 인증 정보(쿠키 또는 헤더)를 포함하는 요청을 허용
    }

    // 컨텐츠 협상 설정
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .defaultContentType(MediaType.APPLICATION_JSON)  // 기본 응답 타입을 JSON으로 설정
                .favorParameter(false)      // URL 파라미터 방식 비활성화 (?format=xml)
                .ignoreAcceptHeader(false); // Accept 헤더 인식 (HTTP 표준 방식 사용)
    }

    // 마이크로초 제거
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }
}
