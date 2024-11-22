package com.zeroone.ktsp.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateBoardDTO {
    private long id;
    private String title;
    private String content;
}
