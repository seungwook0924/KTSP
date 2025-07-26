package com.seungwook.ktsp.domain.file.dto;

import lombok.Getter;

@Getter
public class AttachedFileInfo {

    private final String fileName;

    private final String downloadPath;

    private final String extension;

    private final double kb;

    public AttachedFileInfo(String fileName, String downloadPath, String extension, double kb) {
        this.fileName = fileName;
        this.downloadPath = downloadPath;
        this.extension = extension;
        this.kb = kb;
    }
}
