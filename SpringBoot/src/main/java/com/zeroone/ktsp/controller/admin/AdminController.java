package com.zeroone.ktsp.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController
{
    @Value("${app.log.directory:/spring-boot/log}") //app.log.directory 가 설정되지 않을 경우 기본 경로 설정
    private String LOG_DIRECTORY;

    @GetMapping
    public String showAdminPage(Model model)
    {
        File logDir = new File(LOG_DIRECTORY);
        List<String> logFiles = new ArrayList<>();

        // log 디렉토리에서 모든 로그 파일을 찾기
        if (logDir.exists() && logDir.isDirectory())
        {
            for (File file : logDir.listFiles()) if (file.isFile() && file.getName().startsWith("info-")) logFiles.add(file.getName());
        }

        // 로그 파일 이름을 날짜와 순번에 따라 정렬
        DateTimeFormatter logFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        logFiles.sort(Comparator.comparing((String fileName) -> {
            String datePart = fileName.substring(5, 15); // 날짜 부분 추출
            return LocalDate.parse(datePart, logFormatter);
        }).thenComparingInt(fileName -> {
            String[] parts = fileName.split("\\."); // 순번 추출
            return Integer.parseInt(parts[1]);
        }));

        model.addAttribute("logFiles", logFiles);

        return "admin_view/admin_home";
    }
}
