package com.seungwook.ktsp.domain.board.common.service;

import com.seungwook.ktsp.domain.board.common.entity.Board;
import com.seungwook.ktsp.domain.file.entity.BoardFile;
import com.seungwook.ktsp.domain.file.entity.UploadFile;
import com.seungwook.ktsp.domain.file.service.domain.BoardFileDomainService;
import com.seungwook.ktsp.domain.file.service.domain.UploadFileDomainService;
import com.seungwook.ktsp.domain.file.service.policy.FileStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 이미지 및 첨부파일을 게시글과 연결하는 클래스
@Slf4j
@Service
@RequiredArgsConstructor
public class BoardFileBindingService {

    private final FileStoreService fileStoreService;
    private final UploadFileDomainService uploadFileDomainService;
    private final BoardFileDomainService boardFileDomainService;

    // 게시글 생성시 연결
    @Transactional
    public void bindFilesToBoard(Board board, String content, List<String> attachedFiles) {

        // 이미지 + 첨부파일 uuid 통합
        Set<String> requestedUuids = Stream.concat(
                new HashSet<>(extractImageUUIDs(content)).stream(), // 요청 이미지 uuid 추출(Set으로 중복 제거)
                new HashSet<>(attachedFiles).stream()) // 요청 첨부파일 uuid(Set으로 중복 제거)
                .collect(Collectors.toSet());

        // UploadFile 조회 및 BoardFile 생성
        if (!requestedUuids.isEmpty()) {

            // uuid를 비교하여 실제 존재하는 것만 가져옴
            List<UploadFile> filesToAdd = uploadFileDomainService.findByUuidIn(requestedUuids.stream().toList());

            for (UploadFile file : filesToAdd) {
                BoardFile newBoardFile = BoardFile.createBoardFile(board, file);
                boardFileDomainService.save(newBoardFile);
            }
        }
    }

    // 게시글 수정시 연결
    @Transactional
    public void updateBoundFiles(Board board, String content, List<String> attachedFiles) {

        // 기존 연결된 BoardFile 조회
        List<BoardFile> existingBoardFiles = boardFileDomainService.findByBoard(board);
        Set<String> existingUuids = existingBoardFiles.stream()
                .map(bf -> bf.getFile().getUuid())
                .collect(Collectors.toSet());

        // 이미지 + 첨부파일 uuid 통합
        Set<String> requestedUuids = Stream.concat(
                new HashSet<>(extractImageUUIDs(content)).stream(), // 요청 이미지 uuid 추출(Set으로 중복 제거)
                new HashSet<>(attachedFiles).stream()) // 요청 첨부파일 uuid(Set으로 중복 제거)
                .collect(Collectors.toSet());

        // 삭제 대상(기존 파일 목록중 새로 요청한 파일에 없는 것) 추출
        List<BoardFile> boardFilesToDelete = existingBoardFiles.stream()
                .filter(bf -> !requestedUuids.contains(bf.getFile().getUuid()))
                .toList();

        // 추가 대상(새로 요청한 파일 목록중 기존파일에 없는 것) UUID 추출
        Set<String> uuidsToAdd = requestedUuids.stream()
                .filter(uuid -> !existingUuids.contains(uuid))
                .collect(Collectors.toSet());

        // BoardFile 삭제 (UploadFile도 자동 삭제)
        for (BoardFile boardFile : boardFilesToDelete) {
            boardFileDomainService.delete(boardFile);
        }

        // UploadFile 조회 및 BoardFile 생성
        if (!uuidsToAdd.isEmpty()) {

            // uuid를 비교하여 실제 존재하는 것만 가져옴
            List<UploadFile> filesToAdd = uploadFileDomainService.findByUuidIn(uuidsToAdd.stream().toList());

            for (UploadFile file : filesToAdd) {
                BoardFile newBoardFile = BoardFile.createBoardFile(board, file);
                boardFileDomainService.save(newBoardFile);
            }
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

