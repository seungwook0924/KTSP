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
public class ViewController
{
    private final BoardService boardService;
    private final CommentService commentService;
    private final TeamService teamService;
    private final FileService fileService;
    private final MethodUtil methodUtil;

    //게시글 상세보기
    @GetMapping("/{id}")
    public String addBoard(@PathVariable long id, Model model, HttpSession session, @RequestParam String sidebarType, @RequestParam String boardType)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy년 MM월 dd일 hh시 mm분 ss초"); // 포맷터로 작성일 포맷 변경
        Optional<Board> findBoard = boardService.findById(id);
        if(findBoard.isEmpty()) return "redirect:/learning_core/mentor";

        User user = methodUtil.getSessionUser(session);
        Board board = findBoard.get();
        List<Comment> findComments = commentService.findCommentsByBoard(board);

        List<CommentDTO> comments = new ArrayList<>();
        if (!findComments.isEmpty()) {
            for (Comment comment : findComments)
            {
                CommentDTO commentDTO = toCommentDTO(comment);
                comments.add(commentDTO);
            }
        }

        // 세션에서 viewedBoards 가져오기
        @SuppressWarnings("unchecked") // session.getAttribute가 Object 타입을 반환하기 때문에 캐스팅 시 경고를 억제(조회수 관련 로직)
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
        if(board.getUpdatedAt() == null) boardViewDTO.setUpdatedAt("없음"); // 게시글을 처음 생성할 때는 업데이트 날짜는 없음
        else boardViewDTO.setUpdatedAt(board.getUpdatedAt().format(formatter));

        if(board.getIsClosed() || (board.getUser().getId() == user.getId())) boardViewDTO.setJoin(false); // 게시글이 마감되었거나, 작성자라면 지원 버튼을 비활성화
        else boardViewDTO.setJoin(true); // 그 외의 경우에는 지원 버튼 활성화

        if(board.getUser().getId() == user.getId()) boardViewDTO.setMine(true); // 현재 접속자가 게시글 작성자인지 확인하는 로직
        else boardViewDTO.setMine(false);

        List<FileMapping> files = fileService.getFilesByBoard(board); // 게시글에 매핑된 파일들을 불러옴
        if(!files.isEmpty())
        {
            List<String> fileNames = new ArrayList<>();
            List<String> filePaths = new ArrayList<>();
            for(FileMapping file : files)
            {
                fileNames.add(file.getFileName());
                if (file.getExtension().equals("jpg") || file.getExtension().equals("jpeg") || file.getExtension().equals("png")) // 이미지라면 화면에 표시하기 위해
                {
                    filePaths.add("/files/" + file.getUuid() + "." + file.getExtension()); // 파일 저장 경로를 담음
                }
            }
            boardViewDTO.setFiles(fileNames); // 파일들의 이름을 담음
            boardViewDTO.setImagePath(filePaths); // 이미지 파일의 경로를 담음
        }

        boardViewDTO.setId(board.getId());
        boardViewDTO.setTitle(board.getTitle());
        boardViewDTO.setContent(HtmlUtils.htmlEscape(board.getContent()).replace("\n", "<br>")); // 줄바꿈 변환 처리
        boardViewDTO.setHits(board.getHits());
        boardViewDTO.setClosed(board.getIsClosed());
        boardViewDTO.setCurrentSize(teamService.countValidTeamsByBoardId(board.getId()));
        boardViewDTO.setCreatedAt(board.getCreatedAt().format(formatter));
        boardViewDTO.setTeamSize(board.getTeamSize());
        boardViewDTO.setClosed(board.getIsClosed());
        boardViewDTO.setUserName(board.getUser().getName());
        boardViewDTO.setType(board.getType());


        if(methodUtil.getSessionUser(session).getRole().equals((UserRole.admin))) model.addAttribute("isAdmin", true);

        model.addAttribute("isClosed", board.getIsClosed());
        model.addAttribute("comments", comments);
        model.addAttribute("boardViewDTO", boardViewDTO);
        model.addAttribute("sidebarType", sidebarType);
        model.addAttribute("currentMenu", boardType);
        model.addAttribute("joinTeamDTO", new JoinTeamDTO());
        return "view";
    }

    // Comment 엔티티를 CommentDTO로 변환하는 메서드
    private CommentDTO toCommentDTO(Comment comment)
    {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId(comment.getId());
        commentDTO.setComment(comment.getComment());
        commentDTO.setUserName(comment.getUser().getName());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setMajor(comment.getUser().getMajor());
        commentDTO.setLevelName(comment.getUser().getLevel()); // DTO의 levelName 설정 메서드 활용
        commentDTO.setParentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null);

        // 자식 댓글을 변환하여 childCommentDTOs에 추가
        if (comment.getChildComments() != null && !comment.getChildComments().isEmpty())
        {
            for (Comment child : comment.getChildComments()) commentDTO.getChildCommentDTOs().add(toCommentDTO(child)); // 자식 댓글 변환 후 추가
        }

        return commentDTO;
    }
}
