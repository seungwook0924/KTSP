package com.seungwook.ktsp.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "API 오류 응답 형식")
public class ErrorResponse {

    @Builder.Default
    @Schema(description = "요청 처리 성공 여부", example = "false")
    private Boolean success = false;

    @Schema(description = "오류 메시지", example = "요청을 처리할 수 없습니다.")
    private String message;

    @Schema(description = "에러 추적용 ID", example = "2cd4bb7c")
    private String errorId;

    @Schema(description = "응답 시간", example = "2025-07-14T12:34:56")
    @Builder.Default
    private LocalDateTime responseTime = LocalDateTime.now();
}
