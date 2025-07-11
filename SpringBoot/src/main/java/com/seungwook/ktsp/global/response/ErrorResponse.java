package com.seungwook.ktsp.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String message;

    private String errorId;

    @Builder.Default
    private LocalDateTime responseTime = LocalDateTime.now();
}
