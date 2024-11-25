package com.zeroone.ktsp.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ManagementDTO {
    private long TeamId;
    private long WaitingId;
    private String name;
    private String tel;
    private String major;
    private String content; //소개글
}
