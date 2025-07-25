package com.seungwook.ktsp.domain.file.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private UploadFile(String originalName, String extension) {
        this.originalName = originalName;
        this.uuid = UUID.randomUUID().toString();
        this.extension = extension;
    }

    public static UploadFile createUploadFile(String originalName, String type) {
        return new UploadFile(originalName, type);
    }
}
