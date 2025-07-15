package com.seungwook.ktsp.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)  // null 값은 JSON에 포함하지 않음
@Schema(description = "표준 API 응답 구조")
public class Response<T> {

    @Builder.Default
    @Schema(description = "요청 처리 성공 여부", example = "true")
    private final boolean success = true;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private final String message;

    @Schema(description = "응답 데이터")
    private final T data;

    @Builder.Default
    @Schema(description = "응답 시간", example = "2025-07-14T12:00:00")
    private final LocalDateTime responseTime = LocalDateTime.now();
}
