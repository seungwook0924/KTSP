package com.seungwook.ktsp.domain.board.common.file.support;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileDirectoryInitializer {

    @Value("${file.local-storage.board-directory}")
    private String directoryPath;


    @PostConstruct
    public void fileDirectoryInit() {

        File directory = new File(directoryPath);
        if (!directory.exists() && !directory.mkdirs())
            throw new RuntimeException("Failed to create the file upload directory.");

    }
}
