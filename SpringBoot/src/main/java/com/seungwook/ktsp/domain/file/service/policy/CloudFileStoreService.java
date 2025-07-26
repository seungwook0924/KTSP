package com.seungwook.ktsp.domain.file.service.policy;

import com.seungwook.ktsp.domain.file.dto.AttachedFile;
import com.seungwook.ktsp.domain.file.dto.AttachedFileInfo;
import com.seungwook.ktsp.domain.file.entity.UploadFile;
import com.seungwook.ktsp.domain.file.service.domain.UploadFileDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;

import static com.seungwook.ktsp.domain.file.utils.FileNameUtils.*;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@RequiredArgsConstructor
public class CloudFileStoreService implements FileStoreService{

    @Value("${file.cloud-storage.bucket}")
    private String bucket;

    @Value("${file.cloud-storage.access-url-prefix}")
    private String accessUrlPrefix;

    @Value("${file.download-url-prefix}")
    private String downloadUrlPrefix;

    private final S3Client s3Client;
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
        UploadFile uploadFile = UploadFile.createUploadFile(fileName, extension, file.getSize());

        // key = uuid + 확장자
        String key = uploadFile.getUuid() + ensureDotPrefix(extension);

        // 파일 저장
        saveFile(file, key);

        // UploadFile 객체 저장
        uploadFileDomainService.save(uploadFile);

        return uploadFile;
    }

    @Override
    public void deleteFile(UploadFile uploadFile) {
        String key = uploadFile.getUuid() + ensureDotPrefix(uploadFile.getExtension());

        s3Client.deleteObject(builder -> builder
                .bucket(bucket)
                .key(key)
                .build());
    }

    @Override
    public AttachedFile downloadFile(UploadFile uploadFile) {

        String key = uploadFile.getUuid() + ensureDotPrefix(uploadFile.getExtension());

        ResponseInputStream<GetObjectResponse> file = s3Client.getObject(builder -> builder
                .bucket(bucket)
                .key(key)
                .build());

        return toAttachedFile(file, uploadFile, key);
    }

    @Override
    public String getAccessUrlPrefix() {
        return accessUrlPrefix;
    }

    @Override
    public AttachedFileInfo getAttachedFileInfo(UploadFile uploadFile) {
        return new AttachedFileInfo(uploadFile.getOriginalName(), downloadUrlPrefix + uploadFile.getUuid(), uploadFile.getExtension(), uploadFile.getKiloByte());
    }

    // 파일 저장
    private void saveFile(MultipartFile file, String key) {
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException | S3Exception e) {
            log.error("파일 업로드 중 IOException 발생", e);
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    // 파일 다운로드
    private AttachedFile toAttachedFile(ResponseInputStream<GetObjectResponse> file, UploadFile uploadFile, String key) {
        try {
            byte[] fileContent = file.readAllBytes();
            String contentType = file.response().contentType();

            // 파일 이름 URL 인코딩
            String encodedFileName = encode(uploadFile.getOriginalName() + ensureDotPrefix(uploadFile.getExtension()), UTF_8).replace("+", "%20"); // 브라우저에서 공백 처리

            return new AttachedFile(fileContent, contentType, encodedFileName);
        } catch (IOException e) {
            log.error("파일 다운로드 실패 - key: {}", key, e);
            throw new RuntimeException("첨부파일 다운로드 요청 실패", e);
        }
    }
}
