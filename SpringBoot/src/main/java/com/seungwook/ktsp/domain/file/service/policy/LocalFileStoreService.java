package com.seungwook.ktsp.domain.file.service.policy;

import com.seungwook.ktsp.domain.file.dto.AttachedFile;
import com.seungwook.ktsp.domain.file.dto.AttachedFileInfo;
import com.seungwook.ktsp.domain.file.entity.UploadFile;
import com.seungwook.ktsp.domain.file.exception.FileException;
import com.seungwook.ktsp.domain.file.service.domain.UploadFileDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.seungwook.ktsp.domain.file.utils.FileNameUtils.*;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.probeContentType;
import static java.nio.file.Files.readAllBytes;

@Slf4j
@MainStorePolicy
@RequiredArgsConstructor
public class LocalFileStoreService implements FileStoreService {

    @Value("${file.local-storage.board-directory}")
    private String directoryPath;

    @Value("${file.local-storage.access-url-prefix}")
    private String accessUrlPrefix;

    @Value("${file.local-storage.download-url-prefix}")
    private String downloadUrlPrefix;

    private final UploadFileDomainService uploadFileDomainService;

    @Override
    public UploadFile storeFile(MultipartFile file, boolean isImageFile) {

        // MultipartFile 에서 원본 이름 추출
        String originalFilename = file.getOriginalFilename();

        // 확장자 검사
        validateFilenameWithExtension(originalFilename);

        // 파일이름만 추출
        String fileName = extractFilenameWithoutExtension(originalFilename);

        // 확장자만 추출
        String extension = extractExtension(originalFilename);

        // 이미지 파일이라면 이미지 검증
        if (isImageFile) validateImageExtension(file, extension);

        // UploadFile 객체 생성
        UploadFile uploadFile = UploadFile.createUploadFile(fileName, extension);

        // 파일 경로 생성
        Path path = Paths.get(directoryPath, uploadFile.getUuid() + ensureDotPrefix(extension));

        // 디렉터리 탈출(Path Traversal) 공격 방지
        validatePathInsideDirectory(path, directoryPath);

        // 파일 저장
        saveFile(file, path);

        // UploadFile 객체 저장
        uploadFileDomainService.save(uploadFile);

        return uploadFile;
    }

    @Override
    public void deleteFile(UploadFile uploadFile) {

        // 경로 생성
        Path path = Paths.get(directoryPath, uploadFile.getUuid() + ensureDotPrefix(uploadFile.getType()));

        // 디렉터리 탈출(Path Traversal) 공격 방지
        validatePathInsideDirectory(path, directoryPath);

        File file = path.toFile();

        if (file.exists() && !file.delete())
            throw new FileException("파일 삭제에 실패했습니다.");
    }

    @Override
    public AttachedFile downloadFile(UploadFile uploadFile) {
        File file = new File(directoryPath, uploadFile.getUuid() + ensureDotPrefix(uploadFile.getType()));

        if (!file.exists()) {
            log.warn("존재하지 않은 파일 다운로드 요청 - file: {}", uploadFile.getOriginalName() + "(" + uploadFile.getUuid() + uploadFile.getType() + ")");
            throw new FileException("존재하지 않은 파일입니다.");
        }

        return toAttachedFile(file, uploadFile);
    }

    @Override
    public String getFileAccessPath(UploadFile uploadFile) {

        // 이미지 파일 여부 검사
        if (isImageExtension(uploadFile.getType())) {
            // 경로 생성
            Path path = Paths.get(directoryPath, uploadFile.getUuid() + ensureDotPrefix(uploadFile.getType()));

            // 디렉터리 탈출(Path Traversal) 공격 방지
            validatePathInsideDirectory(path, directoryPath);

            return accessUrlPrefix + uploadFile.getUuid() + ensureDotPrefix(uploadFile.getType());
        }

        return downloadUrlPrefix + uploadFile.getUuid();
    }

    @Override
    public AttachedFileInfo getAttachedFileInfo(UploadFile uploadFile) {
        return new AttachedFileInfo(uploadFile.getOriginalName(), downloadUrlPrefix + uploadFile.getUuid(), uploadFile.getType());
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

    // 파일 다운로드
    private AttachedFile toAttachedFile(File file, UploadFile uploadFile) {
        try {
            byte[] fileContent = readAllBytes(file.toPath());
            String contentType = probeContentType(file.toPath());

            // 파일 이름 URL 인코딩
            String encodedFileName = encode(uploadFile.getOriginalName() + ensureDotPrefix(uploadFile.getType()), UTF_8).replace("+", "%20"); // 브라우저에서 공백 처리

            return new AttachedFile(fileContent, contentType, encodedFileName);

        } catch (IOException e) {
            throw new RuntimeException("첨부파일 다운로드 요청 실패", e);
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
