package com.zeroone.ktsp.controller;


import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.FileMapping;
import com.zeroone.ktsp.service.BoardService;
import com.zeroone.ktsp.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Controller
@RequestMapping("/download")
@RequiredArgsConstructor
@Slf4j
public class DownLoadController
{
    private final FileService fileService;
    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileName") String fileName, @RequestParam("boardId") long id, RedirectAttributes redirectAttributes)
    {
        Optional<Board> findBoard = boardService.findById(id);

        Board board = findBoard.get();

        Optional<FileMapping> findFile = fileService.getFileMappingByBoardAndFileName(board, fileName);
        FileMapping targetFile = findFile.get();

        File file = new File(findFile.get().getPath() + targetFile.getUuid() + "." + targetFile.getExtension());
        log.info("{}", findFile.get().getPath());
        log.info("{}", fileName);
        log.info("{}", file.getAbsolutePath());

        if (!file.exists()) throw new RuntimeException("파일을 찾을 수 없습니다: " + fileName);

        try
        {
            byte[] fileContent = java.nio.file.Files.readAllBytes(file.toPath());
            String contentType = java.nio.file.Files.probeContentType(file.toPath());

            // 파일 이름 URL 인코딩
            String encodedFileName = URLEncoder.encode(fileName , StandardCharsets.UTF_8)
                    .replace("+", "%20"); // 브라우저에서 공백 처리

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName) // 인코딩된 파일 이름
                    .header("Content-Type", contentType)
                    .body(fileContent);
        }
        catch (IOException e)
        {
            throw new RuntimeException("파일 읽기 오류: " + fileName, e);
        }
    }
}
