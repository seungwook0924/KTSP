package com.seungwook.ktsp.domain.board.function.etc.notice.service;

import com.seungwook.ktsp.domain.board.common.support.UuidExtractor;
import com.seungwook.ktsp.domain.board.function.etc.common.dto.request.BoardRegisterRequest;
import com.seungwook.ktsp.domain.board.function.etc.common.dto.request.BoardUpdateRequest;
import com.seungwook.ktsp.domain.board.function.etc.notice.entity.Notice;
import com.seungwook.ktsp.domain.board.function.etc.notice.service.domain.NoticeDomainService;
import com.seungwook.ktsp.domain.file.entity.BoardFile;
import com.seungwook.ktsp.domain.file.entity.UploadFile;
import com.seungwook.ktsp.domain.file.service.domain.BoardFileDomainService;
import com.seungwook.ktsp.domain.file.service.domain.UploadFileDomainService;
import com.seungwook.ktsp.domain.user.entity.User;
import com.seungwook.ktsp.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeCommandService {

    private final NoticeDomainService noticeDomainService;
    private final UserDomainService userDomainService;
    private final UuidExtractor uuidExtractor;
    private final UploadFileDomainService uploadFileDomainService;
    private final BoardFileDomainService boardFileDomainService;

    // 공지사항 등록
    @Transactional
    public void registerNotice(long userId, BoardRegisterRequest request) {

        User user = userDomainService.getReferenceById(userId);

        // 본문에 표시된 이미지 파일 uuid
        List<String> imageFileUuids = uuidExtractor.extractImageUUIDs(request.getContent());

        // 첨부파일로 전송된 파일 uuid
        List<String> attachedFileUuids = request.getAttachedFiles();

        List<UploadFile> images = uploadFileDomainService.findByUuidIn(imageFileUuids);

        List<UploadFile> attachedFiles = uploadFileDomainService.findByUuidIn(attachedFileUuids);

        Notice notice = Notice.createNotice(user, request.getTitle(), request.getContent());
        noticeDomainService.save(notice);

        for (UploadFile file : images) {
            BoardFile boardFile = BoardFile.createBoardFile(notice, file);
            boardFileDomainService.save(boardFile);
        }

        for (UploadFile file : attachedFiles) {
            BoardFile boardFile = BoardFile.createBoardFile(notice, file);
            boardFileDomainService.save(boardFile);
        }
    }

    // 공지사항 수정
    @Transactional
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public void updateNotice(long boardId, BoardUpdateRequest request) {

        Notice notice = noticeDomainService.findAsNotice(boardId);

        notice.updateNotice(request.getTitle(), request.getContent());
    }

    // 공지사항 삭제
    @Transactional
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public void deleteNotice(long boardId) {

        Notice notice = noticeDomainService.findAsNotice(boardId);

        noticeDomainService.delete(notice);
    }
}
