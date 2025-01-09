package com.zeroone.ktsp.controller.pub;

import com.zeroone.ktsp.DTO.AddBoardDTO;
import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.enumeration.BoardType;
import com.zeroone.ktsp.service.BoardService;
import com.zeroone.ktsp.service.FileService;
import com.zeroone.ktsp.service.TeamService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/add_board")
@RequiredArgsConstructor
public class AddBoardController
{
    private final BoardService boardService;
    private final TeamService teamService;
    private final FileService fileService;

    @GetMapping
    public String addBoard(@ModelAttribute AddBoardDTO addBoardDTO, Model model, @RequestParam String sidebarType, @RequestParam String boardType)
    {
        if(boardType.equals("mentor"))
        {
            addBoardDTO.setType(BoardType.mentor);
            addBoardDTO.setTeamSize((byte) 2);
            model.addAttribute("addTitle", "러닝코어 - 가르치미 게시글 작성");
        }
        else if(boardType.equals("mentee"))
        {
            addBoardDTO.setType(BoardType.mentee);
            addBoardDTO.setTeamSize((byte) 2);
            model.addAttribute("addTitle", "러닝코어 - 배우미 게시글 작성");
        }
        else if(boardType.equals("major1"))
        {
            addBoardDTO.setType(BoardType.major1);
            addBoardDTO.setTeamSize((byte) 7);
            model.addAttribute("addTitle", "메이저러너 - 전공 학습 공동체 게시글 작성");
        }
        else if(boardType.equals("major2"))
        {
            addBoardDTO.setType(BoardType.major2);
            addBoardDTO.setTeamSize((byte) 7);
            model.addAttribute("addTitle", "메이저러너 - 신입·재입학생 게시글 작성");
        }
        else if(boardType.equals("major3"))
        {
            addBoardDTO.setType(BoardType.major3);
            addBoardDTO.setTeamSize((byte) 7);
            model.addAttribute("addTitle", "메이저러너 - 전과·편입학생 게시글 작성");
        }
        else if(boardType.equals("projectContest"))
        {
            addBoardDTO.setType(BoardType.project_contest);
            addBoardDTO.setTeamSize((byte) 10);
            model.addAttribute("addTitle", "프로젝트·공모전 게시글 작성");
        }
        else if(boardType.equals("notice"))
        {
            addBoardDTO.setType(BoardType.notice);
            addBoardDTO.setTeamSize((byte) 1);
            model.addAttribute("addTitle", "공지사항 작성");
        }
        else if(boardType.equals("report"))
        {
            addBoardDTO.setType(BoardType.report);
            addBoardDTO.setTeamSize((byte) 1);
            model.addAttribute("addTitle", "불편 접수 게시글 작성");
        }
        else
        {
            addBoardDTO.setType(BoardType.free);
            addBoardDTO.setTeamSize((byte) 1);
            model.addAttribute("addTitle", "자유 게시판 게시글 작성");
        }

        model.addAttribute("sidebarType", sidebarType);
        model.addAttribute("currentMenu", boardType);
        model.addAttribute("addBoardDTO", addBoardDTO);
        return "add_board";
    }

    // 게시글 추가 프로세스
    @PostMapping
    public String saveBoard(@ModelAttribute AddBoardDTO addBoardDTO, HttpSession session, @RequestParam String sidebarType, @RequestParam String boardType)
    {
        User user = (User) session.getAttribute("user");

        // 게시판 저장
        Board newBoard = Board.builder()
                .title(addBoardDTO.getTitle())
                .content(addBoardDTO.getContent())
                .user(user)
                .type(addBoardDTO.getType())
                .createdAt(LocalDateTime.now())
                .isClosed(false)
                .hits(0L)
                .teamSize(addBoardDTO.getTeamSize())
                .build();

        boardService.save(newBoard);
        teamService.save(newBoard, user);

        if (addBoardDTO.getFiles() != null && addBoardDTO.getFiles().stream().anyMatch(file -> !file.isEmpty())) // 첨부 파일이 있다면
        {
            for (MultipartFile file : addBoardDTO.getFiles()) if (!file.isEmpty()) fileService.saveFile(file, newBoard); // 새로운 파일 저장
        }

        return "redirect:/boards/" + newBoard.getId() + "?sidebarType=" + sidebarType + "&boardType=" + boardType;
    }
}
