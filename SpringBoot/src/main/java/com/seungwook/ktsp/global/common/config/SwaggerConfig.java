package com.seungwook.ktsp.global.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("KTSP")
                        .version("1.0.0")
                        .description("KTSP: KNU Talent Sharing Platform")
                        .contact(new Contact()
                                .name("이승욱")
                                .email("leesw_0924@icloud.com")
                                .url("https://github.com/seungwook0924")
                        )
                )
                .servers(List.of(
                        new Server()
                                .url("https://test.seungwook.com")
                                .description("개발 서버"),
                        new Server()
                                .url("https://ktsp.seungwook.com")
                                .description("운영 서버")
                ));
    }
}
