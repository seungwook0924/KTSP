package com.seungwook.ktsp.global.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;

/*
    외부 API 호출을 위한 RestTemplate
*/
@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    @Value("${api.server.url}")
    private String url; // BASE URL

    @Value("${api.server.key}")
    private String key; // API KEY

    @Bean
    public RestTemplate apiRestTemplate()
    {
        return new RestTemplateBuilder()
                .rootUri(url)
                .defaultHeader("X-API-KEY", key) // 모든 요청에 자동으로 API 키 헤더가 포함
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(30))
                .errorHandler(new CustomResponseErrorHandler()) // 커스텀 에러 핸들러 추가
                .build();
    }

    // 커스텀 에러 핸들러
    // 401은 에러로 처리하지 않음 (정상적인 인증 실패 응답)
    private static class CustomResponseErrorHandler extends DefaultResponseErrorHandler
    {
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException
        {
            if (response.getStatusCode().value() == 401) return false;
            return super.hasError(response);
        }
    }
}