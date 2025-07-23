package com.seungwook.ktsp.domain.file.utils;

import com.seungwook.ktsp.domain.file.exception.FileException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileNameUtils {

    private static final Set<String> SUPPORTED_IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");

    // 유효성 검사
    public static void validateFilenameWithExtension(String filename) {
        if (filename == null || !filename.contains("."))
            throw new FileException("유효하지 않은 파일명입니다.");

        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex <= 0 || lastDotIndex == filename.length() - 1)
            throw new FileException("유효하지 않은 파일명입니다.");

        if (filename.contains("/") || filename.contains("\\") || filename.contains(".."))
            throw new FileException("유효하지 않은 파일명입니다.");
    }

    // 확장자 추출
    public static String extractExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    // 파일명 추출
    public static String extractFilenameWithoutExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        return filename.substring(0, lastDotIndex);
    }

    // "." 보정
    public static String ensureDotPrefix(String ext) {
        return ext.startsWith(".") ? ext : "." + ext;
    }

    // 이미지 검증
    public static void validateImageExtension(MultipartFile file, String extension) {
        // 이미지 확장자 검사
        if (!SUPPORTED_IMAGE_EXTENSIONS.contains(extension)) {
            throw new FileException("지원하는 이미지 파일이 아닙니다.");
        }

        // MIME 타입 검증
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/"))
            throw new FileException("잘못된 이미지 파일입니다.");
    }
}
