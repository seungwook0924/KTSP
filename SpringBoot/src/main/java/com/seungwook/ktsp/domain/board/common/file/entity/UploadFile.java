package com.seungwook.ktsp.domain.board.common.file.entity;

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

    @Column(name = "uuid", nullable = false, length = 36, columnDefinition = "CHAR(36)")
    private String uuid;

    @Column(name = "type", nullable = false, length = 20)
    private String type;

    private UploadFile(String originalName, String type) {
        this.originalName = originalName;
        this.uuid = UUID.randomUUID().toString();
        this.type = type;
    }

    public static UploadFile createUploadFile(String originalName, String type) {
        return new UploadFile(originalName, type);
    }
}
