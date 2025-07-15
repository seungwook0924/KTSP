package com.seungwook.ktsp.global.common.controller;

import com.seungwook.ktsp.global.response.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health Check", description = "서버 상태 확인을 위한 헬스 체크 API")
@RestController
public class HealthController {

    @GetMapping("/public/health")
    public ResponseEntity<Response<Void>> healthCheck() {
        return ResponseEntity.ok(Response.<Void>builder()
                .success(true)
                .message("ok")
                .build());
    }
}