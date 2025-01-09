package com.zeroone.ktsp.DTO.mypage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyTeamDTO
{
    private long id; // 팀 id
    private long boardId; // 게시판 id
    private String writer;
    private String boardName;
    private String boardType;
    private String isValid;
}
