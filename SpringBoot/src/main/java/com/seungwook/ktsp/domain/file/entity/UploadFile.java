package com.seungwook.ktsp.domain.file.entity;

import com.seungwook.ktsp.domain.user.entity.User;
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

    // UploadFile과 User는 N:1 관계
    // 여러개의 파일은 한명의 User에 의해 업로드 될 수 있다.
    @ManyToOne(fetch = FetchType.LAZY) // N:1 매핑, 지연로딩
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    private UploadFile(User user, String originalName, String extension, double kiloByte) {
        this.user = user;
        this.originalName = originalName;
        this.uuid = UUID.randomUUID().toString();
        this.extension = extension;
        this.kiloByte = kiloByte;
        this.createdAt = LocalDateTime.now();
    }

    public static UploadFile createUploadFile(User user, String originalName, String type, long byteSize) {
        double kiloByteSize = byteSize / 1024.0; // 바이트 -> 킬로바이트
        double roundedSize = Math.round(kiloByteSize * 100.0) / 100.0; // 소수점 둘째 자리 반올림
        return new UploadFile(user, originalName, type, roundedSize);
    }
}
