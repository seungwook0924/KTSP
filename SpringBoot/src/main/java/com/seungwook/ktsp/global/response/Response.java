package com.seungwook.ktsp.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)  // null 값은 JSON에 포함하지 않음
public class Response<T> {

    @Builder.Default
    private final boolean success = true;

    private final String message;

    private final T data;

    @Builder.Default
    private final LocalDateTime responseTime = LocalDateTime.now();
}
