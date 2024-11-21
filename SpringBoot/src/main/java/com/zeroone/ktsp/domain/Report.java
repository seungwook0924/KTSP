package com.zeroone.ktsp.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Table(name = "reports")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder(toBuilder = true) // 객체 수정 허용
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Report와 User는 N:1 관계
    // 여러 리포트는 하나의 유저가 작성할 수 있다.
    @ManyToOne(fetch = FetchType.LAZY) //연관관계 주인, 지연로딩
    @JoinColumn(name = "user_id")
    private User user;

    // Report와 FileMapping은 1:N 관계
    // 하나의 리포트에 여러 파일 매핑을 가질 수 있다.
    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) // 연관관계 주인이 아님, 지연로딩, 리포트 삭제시 모든 파일 매핑 삭제
    private List<FileMapping> fileMappings;

    // Report와 ReportComment는 1:N 관계
    // 하나의 리포트에 여러 댓글이 달릴 수 있다.
    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) // 연관관계 주인이 아님, 지연로딩, 리포트 삭제시 모든 리포트 댓글 삭제
    private List<ReportComment> reportComments;
}
