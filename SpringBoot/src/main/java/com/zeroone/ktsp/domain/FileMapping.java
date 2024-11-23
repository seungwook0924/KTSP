package com.zeroone.ktsp.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Table(name = "file_mapping")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder(toBuilder = true) // 객체 수정 허용
public class FileMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // FileMapping과 Board는 N:1 관계
    // 여러 파일 매핑은 하나의 게시글과 매핑된다.
    @ManyToOne(fetch = FetchType.LAZY) //1:1 매핑, 지연로딩, 게시글 삭제시 모든 파일 매핑 삭제
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "file_name", nullable = false, length = 50)
    private String fileName;

    @Column(nullable = false, length = 36)
    private String uuid;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false, length = 20)
    private String extension;
}
