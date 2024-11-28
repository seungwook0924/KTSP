package com.zeroone.ktsp.DTO.mypage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyWaitingDTO
{
    private long boardId;
    private String writer;
    private String boardName;
    private String isValid;
    private String boardType;
}
