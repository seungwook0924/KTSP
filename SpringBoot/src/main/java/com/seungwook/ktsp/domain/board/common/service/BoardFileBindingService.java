package com.seungwook.ktsp.domain.board.common.service;

import com.seungwook.ktsp.domain.board.common.entity.Board;
import com.seungwook.ktsp.domain.file.entity.BoardFile;
import com.seungwook.ktsp.domain.file.entity.UploadFile;
import com.seungwook.ktsp.domain.file.service.domain.BoardFileDomainService;
import com.seungwook.ktsp.domain.file.service.domain.UploadFileDomainService;
import com.seungwook.ktsp.domain.file.service.policy.FileStoreService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardFileBindingService {

    private final FileStoreService fileStoreService;
    private final UploadFileDomainService uploadFileDomainService;
    private final BoardFileDomainService boardFileDomainService;

    public void bindFilesToBoard(Board board, String content, List<String> attachedFiles) {

        // 본문에 표시된 이미지 파일 uuid
        List<String> imageFileUuids = extractImageUUIDs(content);

        // 이미지 파일 uuid를 비교하여 실제 존재하는 것만 가져옴
        List<UploadFile> registeredImages = uploadFileDomainService.findByUuidIn(imageFileUuids);

        // 첨부 파일 uuid를 비교하여 실제 존재하는 것만 가져옴
        List<UploadFile> registeredAttachedFiles = uploadFileDomainService.findByUuidIn(attachedFiles);

        // 이미지 파일 등록
        for (UploadFile file : registeredImages) {
            BoardFile boardFile = BoardFile.createBoardFile(board, file);
            boardFileDomainService.save(boardFile);
        }

        // 첨부파일 등록
        for (UploadFile file : registeredAttachedFiles) {
            BoardFile boardFile = BoardFile.createBoardFile(board, file);
            boardFileDomainService.save(boardFile);
        }
    }

    // HTML 이미지 태그에서 uuid 추출
    private List<String> extractImageUUIDs(String htmlContent) {

        List<String> srcList = extractImageSrcList(htmlContent);

        String accessUrlPrefix = fileStoreService.getAccessUrlPrefix();

        return srcList.stream()
                .filter(src -> src.startsWith(accessUrlPrefix))
                .map(src -> {
                    String stripped = src.substring(accessUrlPrefix.length());
                    int dotIndex = stripped.indexOf('.');
                    return dotIndex != -1 ? stripped.substring(0, dotIndex) : stripped;
                })
                .collect(Collectors.toList());
    }

    // HTML 이미지 태그 추출
    private List<String> extractImageSrcList(String htmlContent) {

        Document doc = Jsoup.parse(htmlContent);

        return doc.select("img[src]")
                .stream()
                .map(img -> img.attr("src"))
                .collect(Collectors.toList());
    }
}

