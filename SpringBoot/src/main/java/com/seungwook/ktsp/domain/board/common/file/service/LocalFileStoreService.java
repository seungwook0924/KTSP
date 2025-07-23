package com.seungwook.ktsp.domain.board.common.file.service;

import com.seungwook.ktsp.domain.board.common.file.entity.UploadFile;
import com.seungwook.ktsp.domain.board.common.file.exception.FileException;
import com.seungwook.ktsp.domain.board.common.file.utils.FileNameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@MainStorePolicy
public class LocalFileStoreService implements FileStoreService {

    @Value("${file.local-storage.board-directory}")
    private String directoryPath;

    @Value("${file.local-storage.access-url-prefix}")
    private String accessUrlPrefix;

    @Override
    public UploadFile storeFile(MultipartFile file, boolean isImageFile) {

        // MultipartFile 에서 원본 이름 추출
        String originalFilename = file.getOriginalFilename();

        // 확장자 검사
        FileNameUtils.validateFilenameWithExtension(originalFilename);

        // 파일이름만 추출
        String fileName = FileNameUtils.extractFilenameWithoutExtension(originalFilename);

        // 확장자만 추출
        String extension = FileNameUtils.extractExtension(originalFilename);

        // 이미지 파일이라면 이미지 검증
        if (isImageFile) FileNameUtils.validateImageExtension(file, extension);

        // UploadFile 객체 생성
        UploadFile uploadFile = UploadFile.createUploadFile(fileName, extension);

        // 파일 경로 생성
        Path path = Paths.get(directoryPath, uploadFile.getUuid() + FileNameUtils.ensureDotPrefix(extension));

        // 디렉터리 탈출(Path Traversal) 공격 방지
        validatePathInsideDirectory(path, directoryPath);

        // 파일 저장
        saveFile(file, path);

        return uploadFile;
    }

    @Override
    public void deleteFile(UploadFile uploadFile) {

        // 경로 생성
        Path path = Paths.get(directoryPath, uploadFile.getUuid() + FileNameUtils.ensureDotPrefix(uploadFile.getType()));

        // 디렉터리 탈출(Path Traversal) 공격 방지
        validatePathInsideDirectory(path, directoryPath);

        File file = path.toFile();

        if (file.exists() && !file.delete())
            throw new FileException("파일 삭제에 실패했습니다.");
    }

    @Override
    public String getFileAccessPath(UploadFile uploadFile) {

        // 경로 생성
        Path path = Paths.get(directoryPath, uploadFile.getUuid() + FileNameUtils.ensureDotPrefix(uploadFile.getType()));

        // 디렉터리 탈출(Path Traversal) 공격 방지
        validatePathInsideDirectory(path, directoryPath);

        // 접근 경로 생성
        String relativePath = accessUrlPrefix + uploadFile.getUuid() + FileNameUtils.ensureDotPrefix(uploadFile.getType());

        return ServletUriComponentsBuilder
                .fromCurrentContextPath() // BaseURL을 포함
                .path(relativePath)
                .toUriString();
    }

    // 파일 저장
    private void saveFile(MultipartFile file, Path path) {
        try {
            file.transferTo(path.toFile());
        } catch (IOException e) {
            log.error("파일 저장 실패 - 경로: {}, 원인: {}", path.toAbsolutePath(), e.getMessage());
            throw new RuntimeException("Failed to save the file.", e);
        }
    }

    // 디렉터리 탈출(Path Traversal) 공격 방지
    public static void validatePathInsideDirectory(Path filePath, String baseDirectory) {
        Path normalizedFilePath = filePath.normalize().toAbsolutePath();
        Path normalizedBaseDir = Paths.get(baseDirectory).normalize().toAbsolutePath();

        if (!normalizedFilePath.startsWith(normalizedBaseDir))
            throw new FileException("유효하지 않은 파일 경로입니다.");
    }
}
