package com.zeroone.ktsp.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter
public class UpdateSaveBoardDTO {
    private long id;
    private String title;
    private String content;
    private List<MultipartFile> newFiles; // 새로 추가된 파일 리스트
}
