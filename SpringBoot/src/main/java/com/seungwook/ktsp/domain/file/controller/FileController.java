package com.seungwook.ktsp.domain.file.controller;

import com.seungwook.ktsp.domain.file.dto.AttachedFile;
import com.seungwook.ktsp.domain.file.service.FileService;
import com.seungwook.ktsp.global.auth.support.AuthHandler;
import com.seungwook.ktsp.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "File", description = "File 관련 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/service/file")
public class FileController {

    private final FileService fileService;

    @Operation(summary = "일반 파일 업로드", description = "MultipartFile 데이터 요청")
    @PostMapping
    public ResponseEntity<Response<String>> uploadFile(@Parameter(description = "일반 파일", required = true) @RequestPart("file") MultipartFile file) {

        String response = fileService.uploadFile(AuthHandler.getUserId(), file, false);

        return ResponseEntity.ok(Response.<String>builder()
                .message("일반 파일 업로드 성공")
                .data(response)
                .build());
    }

    @Operation(summary = "이미지 파일 업로드", description = "MultipartFile 데이터 요청")
    @PostMapping("/image")
    public ResponseEntity<Response<String>> uploadImage(@Parameter(description = "이미지 파일", required = true) @RequestPart("file") MultipartFile file) {

        String response = fileService.uploadFile(AuthHandler.getUserId(), file, true);

        return ResponseEntity.ok(Response.<String>builder()
                .message("이미지 파일 업로드 성공")
                .data(response)
                .build());
    }

    @Operation(summary = "파일 다운로드")
    @GetMapping("/{uuid}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String uuid) {

        AttachedFile attachedFile = fileService.downloadFile(uuid);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + attachedFile.getEncodedFileName()) // 인코딩된 파일 이름
                .header("Content-Type", attachedFile.getContentType())
                .body(attachedFile.getFileContent());
    }

    @Operation(summary = "파일 삭제")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Response<Void>> deleteFile(@PathVariable String uuid) {

        fileService.deleteFile(uuid, AuthHandler.getUserId(), AuthHandler.getUserRole());

        return ResponseEntity
                .noContent() // 204
                .build();

    }
}
