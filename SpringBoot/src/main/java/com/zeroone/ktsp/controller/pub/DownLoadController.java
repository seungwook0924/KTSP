package com.zeroone.ktsp.controller.pub;


import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.FileMapping;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.service.BoardService;
import com.zeroone.ktsp.service.FileService;
import com.zeroone.ktsp.util.MethodUtil;
import jakarta.servlet.http.HttpSession;
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
    private final MethodUtil methodUtil;

    @GetMapping
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileName") String fileName, @RequestParam("boardId") long id, HttpSession session)
    {
        User user = methodUtil.getSessionUser(session);
        Optional<Board> findBoard = boardService.findById(id);

        if (findBoard.isPresent())
        {
            Board board = findBoard.get();
            Optional<FileMapping> findFile = fileService.getFileMappingByBoardAndFileName(board, fileName);

            if(findFile.isPresent())
            {
                FileMapping targetFile = findFile.get();

                File file = new File(findFile.get().getPath() + targetFile.getUuid() + "." + targetFile.getExtension());
                log.info("파일 다운로드 - 유저 PK  : {}, 이름 : {}, 게시판 PK : {}, 파일 이름 : {}, 피일 경로 경로 : {}", user.getId(), user.getName(), id, fileName, file.getAbsoluteFile());

                if (!file.exists())
                {
                    log.warn("파일 다운로드 오류 - 서버에 파일이 존재하지 않음. 게시글 pk: {}, 파일 이름: {}", id, fileName);
                    return ResponseEntity.ok().body(new byte[0]); // 빈 byte 반환
                }

                try {
                    byte[] fileContent = java.nio.file.Files.readAllBytes(file.toPath());
                    String contentType = java.nio.file.Files.probeContentType(file.toPath());

                    // 파일 이름 URL 인코딩
                    String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20"); // 브라우저에서 공백 처리

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
            log.warn("파일 다운로드 오류 - 데이터베이스에서 파일을 찾을 수 없음 게시글 pk : {}, 파일 이름 : {}", id, fileName);
            return ResponseEntity.ok().body(new byte[0]); // 빈 byte 반환
        }
        log.warn("파일 다운로드 오류 - 게시글을 찾을 수 없음 게시글 pk : {}", id);
        return ResponseEntity.ok().body(new byte[0]); // 빈 byte 반환
    }
}
