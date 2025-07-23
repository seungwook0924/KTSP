package com.seungwook.ktsp.domain.board.common.file.controller;

import com.seungwook.ktsp.domain.board.common.file.service.FileUploadService;
import com.seungwook.ktsp.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "File", description = "File 관련 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/service/file")
public class FileController {

    private final FileUploadService fileUploadService;

    @Operation(summary = "파일 업로드", description = "일반 파일 업로드")
    @PostMapping
    public ResponseEntity<Response<String>> uploadFile(@Parameter(description = "일반 파일", required = true) @RequestPart("file") MultipartFile file) {

        String response = fileUploadService.uploadFile(file, false);

        return ResponseEntity.ok(Response.<String>builder()
                .message("일반 파일 업로드 성공")
                .data(response)
                .build());
    }

    @Operation(summary = "파일 업로드", description = "이미지 파일 업로드")
    @PostMapping("/image")
    public ResponseEntity<Response<String>> uploadImage(@Parameter(description = "이미지 파일", required = true) @RequestPart("file") MultipartFile file) {

        String response = fileUploadService.uploadFile(file, true);

        return ResponseEntity.ok(Response.<String>builder()
                .message("이미지 파일 업로드 성공")
                .data(response)
                .build());
    }
}
