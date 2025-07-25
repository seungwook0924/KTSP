package com.seungwook.ktsp.domain.board.common.support;

import com.seungwook.ktsp.domain.file.service.policy.FileStoreService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UuidExtractor {

    private final FileStoreService fileStoreService;

    public List<String> extractImageUUIDs(String htmlContent) {
        List<String> srcList = extractImageSrcList(htmlContent);
        String accessUrlPrefix = fileStoreService.getAccessUrlPrefix();
        return srcList.stream()
                .filter(src -> src.startsWith(accessUrlPrefix))
                .map(src -> {
                    String stripped = src.substring(accessUrlPrefix.length());
                    int dotIndex = stripped.indexOf('.');
                    return dotIndex != -1 ? stripped.substring(0, dotIndex) : stripped;
                })
                .collect(Collectors.toList());
    }

    private List<String> extractImageSrcList(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        return doc.select("img[src]")
                .stream()
                .map(img -> img.attr("src"))
                .collect(Collectors.toList());
    }
}

