package com.zeroone.ktsp.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class UpdateViewBoardDTO {
    private long id;
    private String title;
    private String content;
    private List<String> fileNames;
}
