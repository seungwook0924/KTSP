package com.seungwook.ktsp.domain.file.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "upload_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_name", nullable = false, length = 255)
    private String originalName;

    @Column(name = "uuid", nullable = false, length = 36, columnDefinition = "CHAR(36)", unique = true)
    private String uuid;

    @Column(name = "extension", nullable = false, length = 20)
    private String extension;

    @Column(name = "kb", nullable = false)
    private Double kiloByte;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private UploadFile(String originalName, String extension, double kiloByte) {
        this.originalName = originalName;
        this.uuid = UUID.randomUUID().toString();
        this.extension = extension;
        this.kiloByte = kiloByte;
        this.createdAt = LocalDateTime.now();
    }

    public static UploadFile createUploadFile(String originalName, String type, long byteSize) {
        double megaByteSize = byteSize / 1024.0; // 바이트 -> 킬로바이트
        double roundedSize = Math.round(megaByteSize * 100.0) / 100.0; // 소수점 둘째 자리 반올림
        return new UploadFile(originalName, type, roundedSize);
    }
}
