package com.zeroone.ktsp.controller;

import com.zeroone.ktsp.DTO.BoardDTO;
import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.enumeration.BoardType;
import com.zeroone.ktsp.enumeration.UserRole;
import com.zeroone.ktsp.service.BoardService;
import com.zeroone.ktsp.service.TeamService;
import com.zeroone.ktsp.util.MethodUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoriesController {
    private final BoardService boardService;
    private final TeamService teamService;
    private final MethodUtil methodUtil;

    // 게시글 목록
    @GetMapping("/mentor")
    public String showMentor(Model model)
    {
        List<BoardDTO> boardsList = getBoardsList(BoardType.mentor);
        setModelAttributes(model, "러닝코어 - 가르치미", "learningCore", "mentor", boardsList);
        return "view_list";
    }

    @GetMapping("/mentee")
    public String showMentee(Model model)
    {
        List<BoardDTO> boardsList = getBoardsList(BoardType.mentee);
        setModelAttributes(model, "러닝코어 - 배우미", "learningCore", "mentee", boardsList);
        return "view_list";
    }

    @GetMapping("/major1")
    public String showMajor1(Model model)
    {
        List<BoardDTO> boardsList = getBoardsList(BoardType.major1);
        setModelAttributes(model, "메이저러너 - 전공 학습 공동체", "majorLearner", "major1", boardsList);
        return "view_list";
    }

    @GetMapping("/major2")
    public String showMajor2(Model model)
    {
        List<BoardDTO> boardsList = getBoardsList(BoardType.major2);
        setModelAttributes(model, "메이저러너 - 신입·재입학 공동체", "majorLearner", "major2", boardsList);
        return "view_list";
    }

    @GetMapping("/major3")
    public String showMajor3(Model model)
    {
        List<BoardDTO> boardsList = getBoardsList(BoardType.major3);
        setModelAttributes(model, "메이저러너 - 전과·편입학생 공동체", "majorLearner", "major3", boardsList);
        return "view_list";
    }

    @GetMapping("/projectContest")
    public String showProjectContest(Model model)
    {
        List<BoardDTO> boardsList = getBoardsList(BoardType.project_contest);
        setModelAttributes(model, "프로젝트·콘테스트", "projectContest", "projectContest", boardsList);
        return "view_list";
    }

    @GetMapping("/notice")
    public String showNotice(Model model, HttpSession session)
    {
        List<BoardDTO> boardsList = getBoardsList(BoardType.notice);
        User user = methodUtil.getSessionUser(session);
        if(user.getRole().equals(UserRole.admin)) model.addAttribute("isAdmin", true);
        setModelAttributes(model, "공지사항", "community", "notice", boardsList);
        return "view_list";
    }

    @GetMapping("/report")
    public String showReport(Model model)
    {
        List<BoardDTO> boardsList = getBoardsList(BoardType.report);
        setModelAttributes(model, "불편 접수", "community", "report", boardsList);
        return "view_list";
    }

    // 공통 Model 설정 메서드
    private void setModelAttributes(Model model, String title, String sidebarType, String currentMenu, List<BoardDTO> boards)
    {
        model.addAttribute("title", title);
        model.addAttribute("sidebarType", sidebarType);
        model.addAttribute("boards", boards);
        model.addAttribute("currentMenu", currentMenu);
    }

    // Board 리스트 생성 메서드
    private List<BoardDTO> getBoardsList(BoardType boardType)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
        List<Board> boards = boardService.findBoardsByType(boardType);
        boards.sort(Comparator.comparing(Board::getId).reversed());

        ArrayList<BoardDTO> dtoList = new ArrayList<>();
        for (Board board : boards) {
            BoardDTO newDto = new BoardDTO();
            newDto.setId(board.getId());
            newDto.setUserName(board.getUser().getName());
            newDto.setTitle(board.getTitle());
            newDto.setTeamSize(board.getTeamSize());
            newDto.setCurrentSize(teamService.countValidTeamsByBoardId(board.getId()));
            newDto.setHits(board.getHits());
            newDto.setDate(board.getCreatedAt().format(formatter));
            newDto.setClosed(board.getIsClosed());
            dtoList.add(newDto);
        }
        return dtoList;
    }
}
