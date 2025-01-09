package com.zeroone.ktsp.controller.pub;

import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.Comment;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.enumeration.UserRole;
import com.zeroone.ktsp.service.BoardService;
import com.zeroone.ktsp.service.CommentService;
import com.zeroone.ktsp.util.MethodUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Slf4j
public class CommentController
{
    private final CommentService commentService;
    private final BoardService boardService;
    private final MethodUtil methodUtil;

    @PostMapping("/add")
    public ResponseEntity<String> addComment(@RequestParam String content, @RequestParam Long boardId, HttpSession session)
    {
        User user = methodUtil.getSessionUser(session);
        Optional<Board> findBoard = boardService.findById(boardId);
        if(findBoard.isEmpty()) return ResponseEntity.status(404).body("Board not found");
        Board board = findBoard.get();
        commentService.save(content, user, board);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/addReply")
    public ResponseEntity<String> addReply(@RequestParam String content, @RequestParam Long boardId, @RequestParam Long parentCommentId, HttpSession session)
    {
        User user = methodUtil.getSessionUser(session);
        Optional<Board> findBoard = boardService.findById(boardId);
        if(findBoard.isEmpty()) return ResponseEntity.status(404).body("게시글을 찾을 수 없습니다.");
        Board board = findBoard.get();

        Optional<Comment> findParentComment = commentService.findById(parentCommentId);
        if(findParentComment.isEmpty()) return ResponseEntity.status(404).body("부모 댓글을 찾을 수 없습니다.");
        Comment parentComment = findParentComment.get();

        commentService.saveReply(content, user, board, parentComment);
        return ResponseEntity.ok("대댓글이 성공적으로 작성되었습니다.");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteComment(@RequestParam Long commentId, @RequestParam Long boardId, HttpSession session)
    {
        User user = methodUtil.getSessionUser(session);
        if(user.getRole() != UserRole.admin)
        {
            log.warn("댓글 삭제 권한 부족 - 이름 : {} / PK : {}", user.getName(), user.getId());
            return ResponseEntity.status(400).body("권한이 부족합니다.");
        }

        Optional<Board> findBoard = boardService.findById(boardId);
        if(findBoard.isEmpty())
        {
            log.warn("댓글 삭제 오류 - 게시판을 찾을 수 없음. 게시판 PK : {}", boardId);
            return ResponseEntity.status(404).body("Board not found");
        }
        Board board = findBoard.get();

        Optional<Comment> findComment = commentService.findById(commentId);
        if(findComment.isEmpty())
        {
            log.warn("댓글 삭제 오류 - 댓글을 찾을 수 없음 PK : {}", commentId);
            return ResponseEntity.status(404).body("댓글을 찾을 수 없습니다.");
        }
        Comment comment = findComment.get();

        if (comment.getBoard().getId() != board.getId())
        {
            log.warn("댓글 삭제 오류 - 게시판 PK : {} 에 댓글 PK : {} 가 속하지 않음", boardId, commentId);
            return ResponseEntity.status(404).body("댓글이 해당 게시글에 속하지 않습니다.");
        }

        commentService.deleteComment(comment);

        return ResponseEntity.ok("ok");
    }
}
