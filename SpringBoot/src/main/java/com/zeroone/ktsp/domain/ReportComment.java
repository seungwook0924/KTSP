package com.zeroone.ktsp.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Table(name = "report_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder(toBuilder = true) // 객체 수정 허용
public class ReportComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // ReportComment와 Report는 N:1 관계
    // 여러 리포트 댓글은 하나의 리포트에 작성될 수 있다.
    @ManyToOne(fetch = FetchType.LAZY) //연관관계 주인, 지연로딩
    @JoinColumn(name = "report_id")
    private Report report;
}
