package com.zeroone.ktsp.service;

import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.FileMapping;
import com.zeroone.ktsp.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService
{
    private final FileRepository fileRepository;

    // board_id로 파일 가져오기
    public List<FileMapping> getFilesByBoardId(long boardId) {
        return fileRepository.findByBoardId(boardId);
    }

    // Board 객체로 파일 가져오기
    public List<FileMapping> getFilesByBoard(Board board) {
        return fileRepository.findByBoard(board);
    }

    // 게시글에 등록된 파일명을 반환
    public List<String> getFileNamesByBoard(Board board)
    {
        List<FileMapping> fileMappings = fileRepository.findByBoard(board);
        List<String> fileNames = new ArrayList<>();
        for (FileMapping fileMapping : fileMappings) fileNames.add(fileMapping.getFileName());
        return fileNames;
    }

    public Optional<FileMapping> getFileMappingByBoardAndFileName(Board board, String fileName)
    {
        return fileRepository.findByBoardAndFileName(board, fileName);
    }

    public void deleteFile(Board board, String fileName)
    {
        if(fileRepository.findByBoardAndFileName(board, fileName).isEmpty()) log.warn("파일 삭제 실패 - 게시판 ID : {} 에서 {} 를 찾을 수 없음", board.getId(), fileName);
        FileMapping fileMapping = fileRepository.findByBoardAndFileName(board, fileName).get();

        String absolutePath = fileMapping.getPath() + fileMapping.getUuid() + "." + fileMapping.getExtension();
        File file = new File(absolutePath);
        if(file.exists())
        {
            if(file.delete()) log.info("파일 삭제 성공 : {}", absolutePath);
            else log.warn("파일 삭제 실패 : {}", absolutePath);
        }

        fileRepository.delete(fileMapping);
        log.info("게시판 id : {} 에 {}의 요청으로 인해 파일 삭제 완료 : {} / 원본 파일 이름 : {}", board.getId(), board.getUser().getName(), file.getAbsolutePath(), fileName);
    }

    public void saveFile(MultipartFile multipartFile, Board board)
    {
        if (multipartFile.isEmpty())
        {
            log.warn("빈 파일은 저장하지 않습니다.");
            return;
        }

        try
        {
            String extension = getFileExtension(multipartFile.getOriginalFilename()); // 확장자 추출

            String uuidFileName = UUID.randomUUID().toString(); // 고유 파일 이름 생성 (UUID 값만 저장)
            String uniqueFileName = uuidFileName + "." + extension;

            // 저장 경로 설정
            String projectPath = System.getProperty("user.dir");
            String fileDirectory = projectPath + "/boards-files/";
            File directory = new File(fileDirectory);
            if (!directory.exists()) directory.mkdirs(); // 디렉토리가 존재하지 않으면 디렉토리 생성

            String absolutePath = fileDirectory + uniqueFileName; // 파일 절대 경로

            // 파일 저장
            File destinationFile = new File(absolutePath);
            multipartFile.transferTo(destinationFile);

            // FileMapping 엔티티 생성 및 저장
            FileMapping fileMapping = FileMapping.builder()
                    .board(board)
                    .fileName(multipartFile.getOriginalFilename()) // 사용자가 업로드한 원본 파일 이름
                    .uuid(uuidFileName) // 실제 파일 이름
                    .path(fileDirectory) // 저장 경로
                    .extension(extension) // 확장자
                    .build();

            fileRepository.save(fileMapping);
            log.info("작성자 : {}, 게시판 id : {} 에 파일 저장 완료 : {} / 원본 파일 이름 : {}", board.getUser().getName(), board.getId(), absolutePath, multipartFile.getOriginalFilename());

        }
        catch (IOException e)
        {
            log.warn("파일 저장 실패: {}", multipartFile.getOriginalFilename(), e);
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.");
        }
    }

    // 파일 확장자 추출 메서드
    private String getFileExtension(String filename)
    {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}