package com.zeroone.ktsp.controller.pub;

import com.zeroone.ktsp.DTO.BoardViewDTO;
import com.zeroone.ktsp.DTO.CommentDTO;
import com.zeroone.ktsp.DTO.JoinTeamDTO;
import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.Comment;
import com.zeroone.ktsp.domain.FileMapping;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.enumeration.UserRole;
import com.zeroone.ktsp.service.BoardService;
import com.zeroone.ktsp.service.CommentService;
import com.zeroone.ktsp.service.FileService;
import com.zeroone.ktsp.service.TeamService;
import com.zeroone.ktsp.util.MethodUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class ViewController {
    private final BoardService boardService;
    private final CommentService commentService;
    private final TeamService teamService;
    private final FileService fileService;
    private final MethodUtil methodUtil;

    // 게시글 상세보기
    @GetMapping("/{id}")
    public String addBoard(@PathVariable long id, Model model, HttpSession session, @RequestParam String sidebarType, @RequestParam String boardType)
    {
        if (!methodUtil.isValidSidebarTypeAndMenu(sidebarType, boardType)) return "redirect:/";

        Optional<Board> findBoard = boardService.findById(id);
        if (findBoard.isEmpty()) return "redirect:/" + boardType;

        Board board = findBoard.get();
        User user = methodUtil.getSessionUser(session);
        BoardViewDTO boardViewDTO = populateBoardViewDTO(board, user, session);

        List<CommentDTO> comments = populateCommentDTOs(commentService.findCommentsByBoard(board)); // 댓글 데이터 변환

        if (methodUtil.getSessionUser(session).getRole().equals(UserRole.admin)) model.addAttribute("isAdmin", true); // 관리자 확인

        // 모델에 데이터 추가
        model.addAttribute("isClosed", board.getIsClosed());
        model.addAttribute("comments", comments);
        model.addAttribute("boardViewDTO", boardViewDTO);
        model.addAttribute("sidebarType", sidebarType);
        model.addAttribute("currentMenu", boardType);
        model.addAttribute("joinTeamDTO", new JoinTeamDTO());

        return "view";
    }

    // BoardViewDTO 생성 및 설정
    private BoardViewDTO populateBoardViewDTO(Board board, User user, HttpSession session)
    {
        BoardViewDTO boardViewDTO = new BoardViewDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy년 MM월 dd일 hh시 mm분 ss초");

        // 조회수 증가 처리
        handleBoardViewCount(board, user, session);

        // Board 데이터를 BoardViewDTO에 설정
        boardViewDTO.setId(board.getId());
        boardViewDTO.setTitle(board.getTitle());
        boardViewDTO.setContent(HtmlUtils.htmlEscape(board.getContent()).replace("\n", "<br>")); // 줄바꿈 변환 처리
        boardViewDTO.setHits(board.getHits());
        boardViewDTO.setClosed(board.getIsClosed());
        boardViewDTO.setCurrentSize(teamService.countValidTeamsByBoardId(board.getId())); // 현재 팀원 수
        boardViewDTO.setCreatedAt(board.getCreatedAt().format(formatter)); // 작성일 포맷
        boardViewDTO.setUpdatedAt(board.getUpdatedAt() == null ? "없음" : board.getUpdatedAt().format(formatter)); // 수정일 포맷
        boardViewDTO.setTeamSize(board.getTeamSize());
        boardViewDTO.setUserName(board.getUser().getName());
        boardViewDTO.setMajor(board.getUser().getMajor());
        boardViewDTO.setLevel(methodUtil.convertUserLevel(board.getUser().getLevel()));
        boardViewDTO.setType(board.getType());
        boardViewDTO.setJoin(!board.getIsClosed() && board.getUser().getId() != user.getId()); // 지원 가능 여부
        boardViewDTO.setMine(board.getUser().getId() == user.getId()); // 사용자가 작성자인지 여부

        // 파일 정보 설정
        populateFileDetails(board, boardViewDTO);

        return boardViewDTO;
    }

    // 조회수 증가 처리
    private void handleBoardViewCount(Board board, User user, HttpSession session)
    {
        @SuppressWarnings("unchecked") // session.getAttribute가 Object 타입을 반환하기 때문에 캐스팅 시 경고를 억제
        List<Long> viewedBoards = (List<Long>) session.getAttribute("viewedBoards");
        if (viewedBoards == null)
        {
            viewedBoards = new ArrayList<>();
            session.setAttribute("viewedBoards", viewedBoards); // viewedBoards가 null이면 새 리스트 생성 및 세션에 저장
        }

        // 세션에 현재 글의 ID가 없고, 게시글 작성자와 현재 세션의 사용자가 같지 않을 때만 조회수 증가
        if (!viewedBoards.contains(board.getId()) && user.getId() != board.getUser().getId())
        {
            board.incrementHits(); // 조회수 증가
            boardService.save(board); // 변경된 조회수 저장
            viewedBoards.add(board.getId()); // 증가된 게시글 ID를 리스트에 추가
        }
    }

    // 파일 정보를 DTO에 설정
    private void populateFileDetails(Board board, BoardViewDTO boardViewDTO)
    {
        List<FileMapping> files = fileService.getFilesByBoard(board); // 게시글에 매핑된 파일들 조회
        if (!files.isEmpty())
        {
            List<String> fileNames = new ArrayList<>();
            List<String> filePaths = new ArrayList<>();
            for (FileMapping file : files)
            {
                fileNames.add(file.getFileName());
                if (isImageFile(file)) filePaths.add("/files/" + file.getUuid() + "." + file.getExtension()); // 이미지 파일 경로 추가
            }
            boardViewDTO.setFiles(fileNames);
            boardViewDTO.setImagePath(filePaths);
        }
    }

    // 이미지 파일 여부 확인
    private boolean isImageFile(FileMapping file)
    {
        String extension = file.getExtension().toLowerCase();
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png"); // 이미지 파일 확장자 체크
    }

    // 댓글 목록을 DTO 목록으로 변환
    private List<CommentDTO> populateCommentDTOs(List<Comment> comments)
    {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        if (!comments.isEmpty())
        {
            for (Comment comment : comments) commentDTOList.add(toCommentDTO(comment)); // 댓글 엔티티를 DTO로 변환하여 추가
        }
        return commentDTOList;
    }

    // Comment 엔티티를 DTO로 변환
    private CommentDTO toCommentDTO(Comment comment)
    {
        CommentDTO commentDTO = new CommentDTO();

        // 기본 댓글 정보 설정
        commentDTO.setId(comment.getId());
        commentDTO.setComment(comment.getComment());
        commentDTO.setUserName(comment.getUser().getName());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setMajor(comment.getUser().getMajor());
        commentDTO.setLevelName(comment.getUser().getLevel()); // 유저 레벨 설정
        commentDTO.setParentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null);

        // 자식 댓글 처리
        if (comment.getChildComments() != null && !comment.getChildComments().isEmpty())
        {
            for (Comment child : comment.getChildComments()) commentDTO.getChildCommentDTOs().add(toCommentDTO(child)); // 자식 댓글 변환 후 추가
        }

        return commentDTO;
    }
}