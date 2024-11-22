package com.zeroone.ktsp.controller;

import com.zeroone.ktsp.DTO.AddBoardDTO;
import com.zeroone.ktsp.DTO.BoardDTO;
import com.zeroone.ktsp.DTO.BoardViewDTO;
import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.enumeration.BoardType;
import com.zeroone.ktsp.service.BoardService;
import com.zeroone.ktsp.service.TeamService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/learning_core/mentor")
@RequiredArgsConstructor
public class LearningCoreController
{
    private final BoardService boardService;
    private final TeamService teamService;


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
        model.addAttribute("mentorBoards", dtoList);
        model.addAttribute("currentMenu", "one");

        return "learning_core/mentor";
    }

    @GetMapping("/add_board")
    public String addBoard(@ModelAttribute AddBoardDTO addBoardDTO, Model model)
    {
        model.addAttribute("currentMenu", "one");
        model.addAttribute("addBoardDTO", addBoardDTO);
        return "learning_core/add_mentor_board";
    }

    @PostMapping("/add_board")
    public String saveBoard(@ModelAttribute AddBoardDTO addBoardDTO, Model model, HttpSession session)
    {
        User user = (User) session.getAttribute("user");
        Board newBoard = Board.builder()
                .title(addBoardDTO.getTitle())
                .content(addBoardDTO.getContent())
                .user(user)
                .type(BoardType.mentor)
                .createdAt(LocalDateTime.now())
                .isClosed(false)
                .hits(0L)
                .teamSize((byte) 2)
                .build();
        boardService.save(newBoard);
        teamService.save(user, newBoard);
        model.addAttribute("currentMenu", "one");
        return "redirect:/learning_core/mentor";
    }

    @GetMapping("/{id}")
    public String addBoard(@PathVariable long id, Model model, HttpSession session)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy년 MM월 dd일 hh시 mm분 ss초");//포맷터로 작성일 포맷 변경
        Optional<Board> findBoard = boardService.findById(id);
        if(findBoard.isEmpty()) return "redirect:/learning_core/mentor";

        User user = (User) session.getAttribute("user");
        Board board = findBoard.get();

        // 세션에서 viewedBoards 가져오기
        @SuppressWarnings("unchecked") // session.getAttribute가 Object 타입을 반환하기 때문에 캐스팅 시 경고를 억제
        List<Long> viewedBoards = (List<Long>) session.getAttribute("viewedBoards");
        if (viewedBoards == null)
        {
            viewedBoards = new ArrayList<>();
            session.setAttribute("viewedBoards", viewedBoards); // viewedBoards가 null이면 새 리스트 생성 및 세션에 저장
        }

        //세션에 현재 글의 id가 없고, 게시글 작성자와 현재 세션의 사용자가 같지 않을 때만 조회수 증가
        if (!viewedBoards.contains(board.getId()) && user.getId() != board.getUser().getId())
        {
            board.incrementHits(); // 조회수 증가
            boardService.save(board);
            viewedBoards.add(board.getId()); // 증가된 게시글 ID를 리스트에 추가
        }


        BoardViewDTO boardViewDTO = new BoardViewDTO();
        if(board.getUpdatedAt() == null) boardViewDTO.setUpdatedAt("없음");
        else boardViewDTO.setUpdatedAt(board.getUpdatedAt().format(formatter));

        if(board.getIsClosed()) boardViewDTO.setJoin(false);
        else boardViewDTO.setJoin(true);

        if(board.getUser().getId() == user.getId()) boardViewDTO.setModify(true);
        else boardViewDTO.setModify(false);

        boardViewDTO.setId(board.getId());
        boardViewDTO.setTitle(board.getTitle());
        boardViewDTO.setContent(board.getContent());
        boardViewDTO.setHits(board.getHits());
        boardViewDTO.setClosed(board.getIsClosed());
        boardViewDTO.setCurrentSize(teamService.getUserCountByBoardId(board.getId()));
        boardViewDTO.setCreatedAt(board.getCreatedAt().format(formatter));
        boardViewDTO.setTeamSize(board.getTeamSize());
        boardViewDTO.setClosed(board.getIsClosed());
        boardViewDTO.setUserName(user.getName());

        model.addAttribute("boardViewDTO", boardViewDTO);
        model.addAttribute("currentMenu", "one");
        return "learning_core/view";
    }

    @GetMapping("/update/{id}")
    public String modify(@PathVariable long id, Model model, HttpSession session, AddBoardDTO addBoardDTO)
    {
        Optional<Board> findBoard = boardService.findById(id);
        if(findBoard.isEmpty()) return "redirect:/learning_core/mentor";
        Board board = findBoard.get();
        addBoardDTO.setTitle(board.getTitle());
        addBoardDTO.setContent(board.getContent());

        model.addAttribute("addBoardDTO", addBoardDTO);
        model.addAttribute("currentMenu", "one");
        return "learning_core/update_mentor_board";
    }

    @PostMapping("/update/{id}")
    public String updateBoard(@PathVariable long id, @ModelAttribute AddBoardDTO addBoardDTO, Model model, HttpSession session)
    {
        Optional<Board> findBoard = boardService.findById(id);
        if(findBoard.isEmpty()) return "redirect:/learning_core/mentor";

        Board updatedBoard = findBoard.get().toBuilder()
                .title(addBoardDTO.getTitle())
                .content(addBoardDTO.getContent())
                .updatedAt(LocalDateTime.now())
                .build();
        boardService.save(updatedBoard);

        model.addAttribute("currentMenu", "one");
        return "redirect:/learning_core/mentor" + id;
    }
}
