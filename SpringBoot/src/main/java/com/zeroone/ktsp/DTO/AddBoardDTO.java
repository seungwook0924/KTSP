package com.zeroone.ktsp.DTO;

import com.zeroone.ktsp.enumeration.BoardType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter
public class AddBoardDTO {
    private String title;
    private String content;
    private List<MultipartFile> files;
    private BoardType type;
    private Byte teamSize;
}
