package com.seungwook.ktsp.global.common.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;

@Configuration
@EnableScheduling // 주기적 파일삭제 스케쥴링
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO) // 페이징 직렬화 오류 방지
public class WebConfig implements WebMvcConfigurer {

    // 실제 파일 저장 경로
    @Value("${file.local-storage.board-directory}")
    private String fileDirectory;

    // 정적 파일(첨부파일) 매핑 경로
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**") // 요청경로 : /files/
                .addResourceLocations("file:" + fileDirectory); // 'file:' -> 로컬 파일 시스템 접두사
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
