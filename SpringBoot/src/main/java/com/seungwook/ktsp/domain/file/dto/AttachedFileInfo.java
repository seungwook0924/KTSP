package com.seungwook.ktsp.domain.file.dto;

import lombok.Getter;

@Getter
public class AttachedFileInfo {

    private final String fileName;

    private final String downloadPath;

    private final String extension;

    public AttachedFileInfo(String fileName, String downloadPath, String extension) {
        this.fileName = fileName;
        this.downloadPath = downloadPath;
        this.extension = extension;
    }
}
