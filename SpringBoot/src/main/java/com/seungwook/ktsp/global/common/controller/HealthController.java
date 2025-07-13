package com.seungwook.ktsp.global.common.controller;

import com.seungwook.ktsp.global.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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