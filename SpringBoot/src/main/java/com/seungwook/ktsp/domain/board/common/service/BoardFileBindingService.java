package com.seungwook.ktsp.domain.board.common.service;

import com.seungwook.ktsp.domain.board.common.entity.Board;
import com.seungwook.ktsp.domain.board.common.utils.UuidExtractor;
import com.seungwook.ktsp.domain.file.entity.BoardFile;
import com.seungwook.ktsp.domain.file.entity.UploadFile;
import com.seungwook.ktsp.domain.file.service.domain.BoardFileDomainService;
import com.seungwook.ktsp.domain.file.service.domain.UploadFileDomainService;
import com.seungwook.ktsp.domain.file.service.policy.FileStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    public void bindFilesToBoard(Board board, String content, List<String> files) {

        // Optional로 래핑
        List<String> attachedFiles = Optional.ofNullable(files).orElse(Collections.emptyList());

        // 이미지 + 첨부파일 uuid 통합
        Set<String> requestedUuids = mergeImageAndAttachmentUuids(content, attachedFiles);

        // UploadFile 조회 및 BoardFile 생성
        addFiles(requestedUuids, board);
    }

    // 게시글 수정시 연결
    @Transactional
    public void updateBoundFiles(Board board, String content, List<String> files) {

        // Optional로 래핑
        List<String> attachedFiles = Optional.ofNullable(files).orElse(Collections.emptyList());

        // 기존 연결된 BoardFile 조회
        List<BoardFile> existingBoardFiles = boardFileDomainService.findByBoard(board);
        Set<String> existingUuids = existingBoardFiles.stream()
                .map(bf -> bf.getFile().getUuid())
                .collect(Collectors.toSet());

        // 이미지 + 첨부파일 uuid 통합
        Set<String> requestedUuids = mergeImageAndAttachmentUuids(content, attachedFiles);

        // 삭제 대상(기존 파일 목록중 새로 요청한 파일에 없는 것) 추출
        List<BoardFile> boardFilesToDelete = existingBoardFiles.stream()
                .filter(bf -> !requestedUuids.contains(bf.getFile().getUuid()))
                .toList();

        // 추가 대상(새로 요청한 파일 목록중 기존파일에 없는 것) UUID 추출
        Set<String> uuidsToAdd = requestedUuids.stream()
                .filter(uuid -> !existingUuids.contains(uuid))
                .collect(Collectors.toSet());

        // 파일 삭제 및 BoardFile 삭제 (UploadFile도 자동 삭제)
        deleteFileAndBoardFile(boardFilesToDelete);

        // UploadFile 조회 및 BoardFile 생성
        addFiles(uuidsToAdd, board);
    }

    // 특정 게시글에 연결된 모든 파일 삭제 로직
    public void deleteBoundFiles(Board board) {

        // 연결된 모든 BoardFile 조회
        List<BoardFile> existingBoardFiles = boardFileDomainService.findByBoard(board);

        // 파일 삭제 및 BoardFile 삭제 (UploadFile도 자동 삭제)
        deleteFileAndBoardFile(existingBoardFiles);
    }

    // 이미지 + 첨부파일 uuid 통합
    private Set<String> mergeImageAndAttachmentUuids(String content, List<String> attachedFiles) {
        return Stream.concat(
                new HashSet<>(UuidExtractor.extractImageUUIDs(content, fileStoreService.getAccessUrlPrefix())).stream(), // 요청 이미지 uuid 추출(Set으로 중복 제거)
                new HashSet<>(attachedFiles).stream()) // 요청 첨부파일 uuid(Set으로 중복 제거)
                .collect(Collectors.toSet());
    }

    // 파일 삭제 및 BoardFile 삭제 (UploadFile도 자동 삭제)
    private void deleteFileAndBoardFile(List<BoardFile> boardFilesToDelete) {
        for (BoardFile boardFile : boardFilesToDelete) {
            boardFileDomainService.delete(boardFile);
            fileStoreService.deleteFile(boardFile.getFile());
        }
    }

    // UploadFile 조회 및 BoardFile 생성
    private void addFiles(Set<String> uuidsToAdd, Board board) {
        if (!uuidsToAdd.isEmpty()) {

            // uuid를 비교하여 실제 존재하는 것만 가져옴
            List<UploadFile> filesToAdd = uploadFileDomainService.findByUuidIn(uuidsToAdd.stream().toList());

            // BoardFile 생성
            for (UploadFile file : filesToAdd) {
                BoardFile newBoardFile = BoardFile.createBoardFile(board, file);
                boardFileDomainService.save(newBoardFile);
            }
        }
    }
}

