package com.zeroone.ktsp.controller;

import com.zeroone.ktsp.DTO.AddBoardDTO;
import com.zeroone.ktsp.DTO.BoardDTO;
import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.enumeration.BoardType;
import com.zeroone.ktsp.service.BoardService;
import com.zeroone.ktsp.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/mentor")
@RequiredArgsConstructor
public class MentorController
{
    private final BoardService boardService;
    private final TeamService teamService;

    //게시글 목록
    @GetMapping
    public String showMentor(Model model)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");//포맷터로 작성일 포맷 변경
        List<Board> mentorBoards = boardService.findBoardsByType(BoardType.mentor);
        mentorBoards.sort(Comparator.comparing(Board::getId).reversed()); // id 기준 내림차순 정렬
        ArrayList<BoardDTO> dtoList = new ArrayList<>();
        for(Board board : mentorBoards)
        {
            BoardDTO newDto = new BoardDTO();
            newDto.setId(board.getId());
            newDto.setUserName(board.getUser().getName());
            newDto.setTitle(board.getTitle());
            newDto.setTeamSize(board.getTeamSize());
            newDto.setCurrentSize(teamService.getUserCountByBoardId(board.getId())); //같은 board에 매핑된 팀원의 수를 불러옴
            newDto.setHits(board.getHits());
            newDto.setDate(board.getCreatedAt().format(formatter)); //포맷터를 이용해서 반환
            newDto.setClosed(false);
            dtoList.add(newDto);
        }

        model.addAttribute("sidebarType", "learningCore");
        model.addAttribute("boards", dtoList);
        model.addAttribute("currentMenu", "mentor");
        return "view_list";
    }
}
