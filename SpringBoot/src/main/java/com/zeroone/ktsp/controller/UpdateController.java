package com.zeroone.ktsp.controller;


import com.zeroone.ktsp.DTO.UpdateSaveBoardDTO;
import com.zeroone.ktsp.DTO.UpdateViewBoardDTO;
import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.service.BoardService;
import com.zeroone.ktsp.service.FileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/update")
@RequiredArgsConstructor
public class UpdateController
{
    private final BoardService boardService;
    private final FileService fileService;

    @GetMapping("/{id}")
    public String modify(@PathVariable long id, Model model, HttpSession session, UpdateViewBoardDTO updateViewBoardDTO, @RequestParam String sidebarType, @RequestParam String boardType)
    {
        Optional<Board> findBoard = boardService.findById(id);
        User user = (User) session.getAttribute("user");
        if(findBoard.isEmpty()) return "redirect:/learning_core/mentor";
        Board board = findBoard.get();

        if(user.getId() != board.getUser().getId()) return "redirect:/"; //본인이 아닌 다른 사람이 접근했을 때 리다이렉트

        updateViewBoardDTO.setId(id);
        updateViewBoardDTO.setTitle(board.getTitle());
        updateViewBoardDTO.setContent(board.getContent());
        updateViewBoardDTO.setFileNames(fileService.getFileNamesByBoard(board));

        model.addAttribute("updateBoardDTO", updateViewBoardDTO);
        model.addAttribute("sidebarType", sidebarType);
        model.addAttribute("currentMenu", boardType);
        return "update_board";
    }

    @PostMapping("/{id}")
    public String updateBoard(@PathVariable long id, @ModelAttribute UpdateSaveBoardDTO updateSaveBoardDTO, @RequestParam String sidebarType, @RequestParam String boardType)
    {
        Optional<Board> findBoard = boardService.findById(id);
        if(findBoard.isEmpty()) return "redirect:/learning_core/mentor";

        Board board = findBoard.get();

        Board updatedBoard = board.toBuilder()
                .title(updateSaveBoardDTO.getTitle())
                .content(updateSaveBoardDTO.getContent())
                .updatedAt(LocalDateTime.now())
                .build();
        boardService.save(updatedBoard);

        if (updateSaveBoardDTO.getFiles() != null && updateSaveBoardDTO.getFiles().stream().anyMatch(file -> !file.isEmpty())) // 첨부 파일이 있다면
        {
            for (MultipartFile file : updateSaveBoardDTO.getFiles()) if (!file.isEmpty()) fileService.saveFile(file, updatedBoard); // 새로운 파일 저장
        }
        return "redirect:/boards/" + board.getId() + "?sidebarType=" + sidebarType + "&boardType=" + boardType;
    }
}
