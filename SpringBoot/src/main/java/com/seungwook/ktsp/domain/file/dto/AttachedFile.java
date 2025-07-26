package com.seungwook.ktsp.domain.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttachedFile {

    // 파일 내용
    private final byte[] fileContent;

    // 컨텐츠 타입
    private final String contentType;

    // 인코딩된 파일 이름
    private final String encodedFileName;
}
