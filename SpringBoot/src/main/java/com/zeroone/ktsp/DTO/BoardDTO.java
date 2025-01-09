package com.zeroone.ktsp.DTO;

import com.zeroone.ktsp.enumeration.BoardType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardDTO {
    private long id;
    private String title;
    private String userName;
    private String date;
    private Byte teamSize;
    private Byte currentSize;
    private long hits;
    private boolean isClosed;
}
