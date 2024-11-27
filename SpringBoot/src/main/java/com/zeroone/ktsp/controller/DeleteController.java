package com.zeroone.ktsp.controller;

import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.enumeration.BoardType;
import com.zeroone.ktsp.service.BoardService;
import com.zeroone.ktsp.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/delete")
@RequiredArgsConstructor
public class DeleteController
{
    private final BoardService boardService;
    private final FileService fileService;

    @PostMapping("/{id}")
    public String deleteBoard(@PathVariable long id, @RequestParam BoardType type, RedirectAttributes redirectAttributes)
    {
        try
        {
            fileService.deleteAllFilesByBoard(id);
            boardService.deleteById(id);
        }
        catch (IllegalArgumentException e)
        {
            redirectAttributes.addFlashAttribute("errorMessage", true);
            return "redirect:learning_core/mentor/" + id;
        }
        if(type.equals(BoardType.mentor)) return "redirect:/" + "mentor";
        else if(type.equals(BoardType.mentee)) return "redirect:/" + "mentee";
        else if(type.equals(BoardType.project_contest)) return "redirect:/" + "project_contest";
        else if(type.equals(BoardType.major1)) return "redirect:/" + "major1";
        else if(type.equals(BoardType.major2)) return "redirect:/" + "major2";
        else if(type.equals(BoardType.major3)) return "redirect:/" + "major3";
        else return "redirect:/" + "notice";
    }

    @DeleteMapping("/delete-file")
    public ResponseEntity<Void> deleteFile(@RequestParam("fileName") String fileName, @RequestParam("boardId") Long boardId)
    {
        Board board = boardService.findById(boardId).orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));
        fileService.deleteFile(board, fileName);
        return ResponseEntity.ok().build();
    }
}
