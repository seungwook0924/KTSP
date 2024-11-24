package com.zeroone.ktsp.controller;

import com.zeroone.ktsp.DTO.*;
import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.FileMapping;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.enumeration.BoardType;
import com.zeroone.ktsp.service.BoardService;
import com.zeroone.ktsp.service.FileService;
import com.zeroone.ktsp.service.TeamService;
import com.zeroone.ktsp.service.WaitingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/learning_core/mentor")
@RequiredArgsConstructor
public class LearningCoreController
{
    private final BoardService boardService;
    private final TeamService teamService;
    private final FileService fileService;
    private final WaitingService watingService;

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
        model.addAttribute("mentorBoards", dtoList);
        model.addAttribute("currentMenu", "one");

        return "learning_core/mentor";
    }

    // 게시글 추가
    @GetMapping("/add_board")
    public String addBoard(@ModelAttribute AddBoardDTO addBoardDTO, Model model)
    {
        model.addAttribute("currentMenu", "one");
        model.addAttribute("addBoardDTO", addBoardDTO);
        return "learning_core/add_mentor_board";
    }

    // 게시글 추가 프로세스
    @PostMapping("/add_board")
    public String saveBoard(@ModelAttribute AddBoardDTO addBoardDTO, Model model, HttpSession session)
    {
        User user = (User) session.getAttribute("user");

        // 게시판 저장
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

        if (addBoardDTO.getFiles() != null && addBoardDTO.getFiles().stream().anyMatch(file -> !file.isEmpty())) // 첨부 파일이 있다면
        {
            for (MultipartFile file : addBoardDTO.getFiles()) if (!file.isEmpty()) fileService.saveFile(file, newBoard); // 새로운 파일 저장
        }

        model.addAttribute("currentMenu", "one");
        return "redirect:/learning_core/mentor/" + newBoard.getId();
    }

    //게시글 상세보기
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

        if(board.getIsClosed() || (board.getUser().getId() == user.getId())) boardViewDTO.setJoin(false);
        else boardViewDTO.setJoin(true);

        if(board.getUser().getId() == user.getId()) boardViewDTO.setMine(true);
        else boardViewDTO.setMine(false);

        List<FileMapping> files = fileService.getFilesByBoard(board);
        if(!files.isEmpty())
        {
            List<String> fileNames = new ArrayList<>();
            List<String> filePaths = new ArrayList<>();
            for(FileMapping file : files)
            {
                fileNames.add(file.getFileName());
                if (file.getExtension().equals("jpg") || file.getExtension().equals("jpeg") || file.getExtension().equals("png"))
                {
                    filePaths.add("/files/" + file.getUuid() + "." + file.getExtension());
                    log.info("/files/{}", file.getUuid() + "." + file.getExtension());
                }
            }
            boardViewDTO.setFiles(fileNames);
            boardViewDTO.setImagePath(filePaths);
        }

        boardViewDTO.setId(board.getId());
        boardViewDTO.setTitle(board.getTitle());
        boardViewDTO.setContent(HtmlUtils.htmlEscape(board.getContent()).replace("\n", "<br>")); // 줄바꿈 변환 처리
        boardViewDTO.setHits(board.getHits());
        boardViewDTO.setClosed(board.getIsClosed());
        boardViewDTO.setCurrentSize(teamService.getUserCountByBoardId(board.getId()));
        boardViewDTO.setCreatedAt(board.getCreatedAt().format(formatter));
        boardViewDTO.setTeamSize(board.getTeamSize());
        boardViewDTO.setClosed(board.getIsClosed());
        boardViewDTO.setUserName(board.getUser().getName());

        model.addAttribute("boardViewDTO", boardViewDTO);
        model.addAttribute("currentMenu", "one");
        model.addAttribute("joinTeamDTO", new JoinTeamDTO());
        return "learning_core/view";
    }

    @GetMapping("/update/{id}")
    public String modify(@PathVariable long id, Model model, HttpSession session, UpdateViewBoardDTO updateViewBoardDTO)
    {
        Optional<Board> findBoard = boardService.findById(id);
        if(findBoard.isEmpty()) return "redirect:/learning_core/mentor";
        Board board = findBoard.get();
        updateViewBoardDTO.setId(id);
        updateViewBoardDTO.setTitle(board.getTitle());
        updateViewBoardDTO.setContent(board.getContent());
        updateViewBoardDTO.setFileNames(fileService.getFileNamesByBoard(board));

        model.addAttribute("updateBoardDTO", updateViewBoardDTO);
        model.addAttribute("currentMenu", "one");
        return "learning_core/update_mentor_board";
    }

    @PostMapping("/update/{id}")
    public String updateBoard(@PathVariable long id, @ModelAttribute UpdateSaveBoardDTO updateSaveBoardDTO, Model model)
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

        model.addAttribute("currentMenu", "one");
        return "redirect:/learning_core/mentor/" + id;
    }

    @DeleteMapping("/delete-file")
    public ResponseEntity<Void> deleteFile(@RequestParam("fileName") String fileName, @RequestParam("boardId") Long boardId)
    {
        Board board = boardService.findById(boardId).orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));
        fileService.deleteFile(board, fileName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete/{id}")
    public String deleteBoard(@PathVariable long id, RedirectAttributes redirectAttributes)
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
        return "redirect:/learning_core/mentor";
    }

    @PostMapping("/join/{boardId}")
    public ResponseEntity<String> joinTeam(@PathVariable Long boardId, @RequestBody JoinTeamDTO joinTeamDTO, HttpSession session)
    {
        User user = (User) session.getAttribute("user");

        String content = joinTeamDTO.getContent();
        if (content == null || content.isBlank()) return ResponseEntity.badRequest().body("내용을 입력해야 합니다.");

        Optional<Board> findBoard = boardService.findById(boardId);
        if (findBoard.isEmpty()) return ResponseEntity.badRequest().body("지원하려는 게시글을 찾을 수 없습니다.");

        Board board = findBoard.get();

        if (watingService.existsByBoardAndUser(board, user)) return ResponseEntity.badRequest().body("이미 지원하셨습니다."); // 이미 지원했는지 확인

        watingService.save(board, user, content);
        return ResponseEntity.ok("지원이 성공적으로 완료되었습니다.");
    }
}
