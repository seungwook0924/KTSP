package com.zeroone.ktsp.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Table(name = "files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder(toBuilder = true) // 객체 수정 허용
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // File과 FileMapping은 1:1 관계
    // 하나의 파일 매핑은 하나의 파일과 매핑된다.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) //연관관계 주인, 지연로딩
    @JoinColumn(name = "board_id")
    private Board board;

}
