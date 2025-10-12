package com.seungwook.ktsp.domain.file.entity;

import com.seungwook.ktsp.domain.board.common.entity.Board;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "board_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // BoardFile과 board는 N:1 관계
    // 여러개의 BoardFile은 하나의 Board에 매핑 될 수 있다.
    @ManyToOne(fetch = FetchType.LAZY) // N:1 매핑, 지연로딩
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // BoardFile과 UploadFile는 1:1 관계
    // 하나의 BoardFile은 하나의 UploadFile에 매핑 될 수 있다.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true) // 1:1 매핑, 지연로딩
    @JoinColumn(name = "file_id", nullable = false, unique = true)
    private UploadFile file;

    // 생성자
    private BoardFile(Board board, UploadFile file) {
        this.board = board;
        this.file = file;
    }

    // 정적 팩터리
    public static BoardFile createBoardFile(Board board, UploadFile file) {
        return new BoardFile(board, file);
    }
}
