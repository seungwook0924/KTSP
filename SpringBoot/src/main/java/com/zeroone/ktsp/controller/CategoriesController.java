package com.zeroone.ktsp.controller;

import com.zeroone.ktsp.DTO.BoardDTO;
import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.enumeration.BoardType;
import com.zeroone.ktsp.enumeration.UserRole;
import com.zeroone.ktsp.service.TeamService;
import com.zeroone.ktsp.service.paganation.PaginatedBoardService;
import com.zeroone.ktsp.util.MethodUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoriesController
{
    private final TeamService teamService;
    private final MethodUtil methodUtil;
    private final PaginatedBoardService paginatedBoardService;

    // 게시글 목록
    @GetMapping("/mentor")
    public String showMentor(Model model, @RequestParam(defaultValue = "0") int page)
    {
        return handleBoardRequest(model, BoardType.mentor, "러닝코어 - 가르치미", "learningCore", "mentor", page);
    }

    @GetMapping("/mentee")
    public String showMentee(Model model, @RequestParam(defaultValue = "0") int page)
    {
        return handleBoardRequest(model, BoardType.mentee, "러닝코어 - 배우미", "learningCore", "mentee", page);
    }

    @GetMapping("/major1")
    public String showMajor1(Model model, @RequestParam(defaultValue = "0") int page)
    {
        return handleBoardRequest(model, BoardType.major1, "메이저러너 - 전공 학습 공동체", "majorLearner", "major1", page);
    }

    @GetMapping("/major2")
    public String showMajor2(Model model, @RequestParam(defaultValue = "0") int page)
    {
        return handleBoardRequest(model, BoardType.major2, "메이저러너 - 신입·재입학생 공동체", "majorLearner", "major2", page);
    }

    @GetMapping("/major3")
    public String showMajor3(Model model, @RequestParam(defaultValue = "0") int page)
    {
        return handleBoardRequest(model, BoardType.major3, "메이저러너 - 전과·편입학생 공동체", "majorLearner", "major3", page);
    }

    @GetMapping("/projectContest")
    public String showProjectContest(Model model, @RequestParam(defaultValue = "0") int page)
    {
        return handleBoardRequest(model, BoardType.project_contest, "프로젝트·공모전", "projectContest", "projectContest", page);
    }

    @GetMapping("/notice")
    public String showNotice(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page)
    {
        User user = methodUtil.getSessionUser(session);
        if (user.getRole().equals(UserRole.admin)) model.addAttribute("isAdmin", true);
        return handleBoardRequest(model, BoardType.notice, "공지사항", "community", "notice", page);
    }

    @GetMapping("/report")
    public String showReport(Model model, @RequestParam(defaultValue = "0") int page)
    {
        return handleBoardRequest(model, BoardType.report, "불편 접수", "community", "report", page);
    }

    @GetMapping("/search")
    public String showSearchResult(Model model, @RequestParam String category, @RequestParam String q, @RequestParam(defaultValue = "0") int page)
    {
        BoardType boardType;
        String title;
        String sidebarType;

        switch (category)
        {
            case "mentor":
                boardType = BoardType.mentor;
                title = "가르치미";
                sidebarType = "learningCore";
                break;
            case "mentee":
                boardType = BoardType.mentee;
                title = "배우미";
                sidebarType = "learningCore";
                break;
            case "major1":
                boardType = BoardType.major1;
                title = "전공 학습 공동체";
                sidebarType = "majorLearner";
                break;
            case "major2":
                boardType = BoardType.major2;
                title = "신입·재입학생 공동체";
                sidebarType = "majorLearner";
                break;
            case "major3":
                boardType = BoardType.major3;
                title = "전과·편입학생 공동체";
                sidebarType = "majorLearner";
                break;
            case "projectContest":
                boardType = BoardType.project_contest;
                title = "프로젝트·공모전";
                sidebarType = "projectContest";
                break;
            default:
                boardType = null; // 전체 검색
                title = "검색 결과";
                sidebarType = "community"; // 기본값
                break;
        }

        Pageable pageable = PageRequest.of(page, 10);
        Page<Board> boardPage;

        boardPage = paginatedBoardService.searchByKeyword(boardType, q, pageable);

        List<BoardDTO> boardsList = convertToBoardDTOList(boardPage);
        setModelAttributes(model, title, sidebarType, category, boardsList, boardPage.getNumber(), boardPage.getTotalPages(), boardPage.getTotalElements(), boardPage.getSize());
        return "view_list";
    }

    // 공통 Board 요청 처리 메서드
    private String handleBoardRequest(Model model, BoardType boardType, String title, String sidebarType, String currentMenu, int page)
    {
        Page<Board> boardPage = getPagedBoards(boardType, page); // 페이지네이션 처리
        List<BoardDTO> boardsList = convertToBoardDTOList(boardPage); // DTO 변환
        setModelAttributes(model, title, sidebarType, currentMenu, boardsList, boardPage.getNumber(), boardPage.getTotalPages(), boardPage.getTotalElements(), boardPage.getSize());
        //getNumber : 현재 페이지 번호, getTotalPages : 총 페이지 수, getTotalElements : 전체 게시글 수, getSize : 한 페이지당 게시글 수
        return "view_list";
    }

    // 페이지네이션된 Board 데이터 가져오기
    private Page<Board> getPagedBoards(BoardType boardType, int page)
    {
        Pageable pageable = PageRequest.of(page, 10); // Pageable 객체 생성, 한 페이지당 최대 10개 게시글
        return paginatedBoardService.findBoardsByTypeWithPaging(boardType, pageable); // 페이지네이션된 데이터 가져오기
    }

    // Board 리스트를 DTO 리스트로 변환
    private List<BoardDTO> convertToBoardDTOList(Page<Board> boardPage)
    {
        List<Board> boards = new ArrayList<>(boardPage.getContent()); // 수정 가능한 리스트로 복사
        boards.sort(Comparator.comparing(Board::getCreatedAt).reversed()); // 최신순(역순)으로 정렬

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
        List<BoardDTO> boardList = new ArrayList<>();
        for (Board board : boards)
        {
            BoardDTO newBoard = new BoardDTO();
            newBoard.setId(board.getId());
            newBoard.setUserName(board.getUser().getName());
            newBoard.setTitle(board.getTitle());
            newBoard.setTeamSize(board.getTeamSize());
            newBoard.setCurrentSize(teamService.countValidTeamsByBoardId(board.getId())); // 현재 팀원 수
            newBoard.setHits(board.getHits());
            newBoard.setDate(board.getCreatedAt().format(formatter));
            newBoard.setClosed(board.getIsClosed());
            boardList.add(newBoard);
        }
        return boardList;
    }

    // 공통 Model 설정 메서드
    private void setModelAttributes(Model model, String title, String sidebarType, String currentMenu, List<BoardDTO> boards, int currentPage, int totalPages, long totalItems, int pageSize)
    {
        model.addAttribute("title", title);
        model.addAttribute("sidebarType", sidebarType);
        model.addAttribute("boards", boards);
        model.addAttribute("currentMenu", currentMenu);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("pageSize", pageSize);
    }
}