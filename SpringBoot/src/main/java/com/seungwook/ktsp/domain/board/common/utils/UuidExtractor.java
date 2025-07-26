package com.seungwook.ktsp.domain.board.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UuidExtractor {

    // HTML 이미지 태그에서 uuid 추출
    public static List<String> extractImageUUIDs(String htmlContent, String accessUrlPrefix) {

        Document doc = Jsoup.parse(htmlContent);

        // HTML 이미지 태그 파싱
        List<String> srcList = doc.select("img[src]")
                .stream()
                .map(img -> img.attr("src"))
                .toList();

        // UUID 추출
        return srcList.stream()
                .filter(src -> src.startsWith(accessUrlPrefix)) // 지정된 accessUrlPrefix로 시작하는 src 속성만 필터링 (유효한 이미지 경로 판별)
                .map(src -> {
                    String stripped = src.substring(accessUrlPrefix.length()); // 접두사 제거 후 파일명(uuid.확장자)만 추출
                    int dotIndex = stripped.indexOf('.'); // 확장자 구분을 위해 '.' 위치 확인
                    return dotIndex != -1 ? stripped.substring(0, dotIndex) : stripped; // 확장자 제거 후 uuid만 추출
                })
                .collect(Collectors.toList());
    }
}
