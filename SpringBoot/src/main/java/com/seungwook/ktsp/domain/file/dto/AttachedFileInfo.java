package com.seungwook.ktsp.domain.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttachedFileInfo {

    private final String fileName;
    private final String downloadPath;
    private final String extension;
    private final double kb;
}
