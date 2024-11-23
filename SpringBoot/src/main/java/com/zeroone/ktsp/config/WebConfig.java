package com.zeroone.ktsp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}") // 프로퍼티 파일에서 경로를 읽어옴
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        // /files/** 요청이 boards-files 디렉토리로 매핑되도록 설정
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + uploadDir);
    }
}
