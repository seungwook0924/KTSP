package com.seungwook.ktsp.domain.file.dto;

import lombok.Getter;

@Getter
public class AttachedFile {

    // 파일 내용
    private final byte[] fileContent;

    // 컨텐츠 타입
    private final String contentType;

    // 인코딩된 파일 이름
    private final String encodedFileName;

    public AttachedFile(byte[] fileContent, String contentType, String encodedFileName) {
        this.fileContent = fileContent;
        this.contentType = contentType;
        this.encodedFileName = encodedFileName;
    }
}
