package com.zeroone.ktsp.DTO;

import com.zeroone.ktsp.enumeration.UserLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
// CommentDTO 클래스
public class CommentDTO
{
    private long id;
    private String userName;
    private String comment;
    private LocalDateTime createdAt;
    private String major;
    private String levelName;
    private Long parentCommentId;
    private List<CommentDTO> childCommentDTOs = new ArrayList<>();

    // levelName 설정 메서드
    public void setLevelName(UserLevel level)
    {
        if (level == UserLevel.etc) this.levelName = null; // etc는 표시하지 않음
        else
        {
            switch (level)
            {
                case freshman -> this.levelName = "1학년";
                case sophomore -> this.levelName = "2학년";
                case junior -> this.levelName = "3학년";
                case senior -> this.levelName = "4학년";
            }
        }
    }
}
