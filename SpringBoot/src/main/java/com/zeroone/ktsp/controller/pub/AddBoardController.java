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
        }
        else if(boardType.equals("mentee"))
        {
            addBoardDTO.setType(BoardType.mentee);
            addBoardDTO.setTeamSize((byte) 2);
        }
        else if(boardType.equals("major1"))
        {
            addBoardDTO.setType(BoardType.major1);
            addBoardDTO.setTeamSize((byte) 6);
        }
        else if(boardType.equals("major2"))
        {
            addBoardDTO.setType(BoardType.major2);
            addBoardDTO.setTeamSize((byte) 6);
        }
        else if(boardType.equals("major3"))
        {
            addBoardDTO.setType(BoardType.major3);
            addBoardDTO.setTeamSize((byte) 6);
        }
        else if(boardType.equals("projectContest"))
        {
            addBoardDTO.setType(BoardType.project_contest);
            addBoardDTO.setTeamSize((byte) 6);
        }
        else addBoardDTO.setType(BoardType.notice);

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
