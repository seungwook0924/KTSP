package com.zeroone.ktsp.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class BoardViewDTO {
    private long id;
    private String title;
    private String content;
    private String userName;
    private String createdAt;
    private String updatedAt;
    private Byte teamSize;
    private Byte currentSize;
    private long hits;
    private boolean isClosed;
    private boolean isMine;
    private boolean isJoin;
    private List<String> files;
    private List<String> imagePath;
}
