package com.zeroone.ktsp.controller;

import com.zeroone.ktsp.DTO.JoinTeamDTO;
import com.zeroone.ktsp.DTO.ManagementDTO;
import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.Team;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.domain.Waiting;
import com.zeroone.ktsp.enumeration.BoardType;
import com.zeroone.ktsp.service.BoardService;
import com.zeroone.ktsp.service.TeamService;
import com.zeroone.ktsp.service.WaitingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController
{
    private final BoardService boardService;
    private final TeamService teamService;
    private final WaitingService waitingService;

    @PostMapping("/join/{boardId}")
    public ResponseEntity<String> joinTeam(@PathVariable Long boardId, @RequestBody JoinTeamDTO joinTeamDTO, HttpSession session)
    {
        User user = (User) session.getAttribute("user");

        String content = joinTeamDTO.getContent();
        if (content == null || content.isBlank()) return ResponseEntity.badRequest().body("내용을 입력해야 합니다.");

        Optional<Board> findBoard = boardService.findById(boardId);
        if (findBoard.isEmpty()) return ResponseEntity.badRequest().body("지원하려는 게시글을 찾을 수 없습니다.");

        Board board = findBoard.get();

        if (waitingService.existsByBoardAndUser(board, user)) return ResponseEntity.badRequest().body("이미 지원하셨습니다. 지원 내역 및 결과는 마이페이지에서 확인할 수 있습니다."); // 이미 지원했는지 확인

        waitingService.save(board, user, content);
        return ResponseEntity.ok("지원이 성공적으로 완료되었습니다.");
    }

    @GetMapping("/manage/{id}")
    public String showManageView(@PathVariable long id, @RequestParam BoardType type, HttpSession session, Model model)
    {
        User user = (User) session.getAttribute("user");
        Optional<Board> findBoard = boardService.findById(id);

        if(findBoard.isEmpty()) return "redirect:/" + type; //게시글이 존재하지 않을 때 리다이렉트
        Board board = findBoard.get();
        if(user.getId() != board.getUser().getId()) return "redirect:/" + type; //본인이 아닌 다른 사람이 접근했을 때 리다이렉트

        List<ManagementDTO> waitingList = new ArrayList<>();
        List<Waiting> allWaitings = waitingService.findAllByBoard(board);
        if(!allWaitings.isEmpty())
        {
            for(Waiting waiting : allWaitings)
            {
                if(!waiting.getIsValid()) continue;
                ManagementDTO waitingDTO = new ManagementDTO();
                waitingDTO.setWaitingId(waiting.getId());
                waitingDTO.setName(waiting.getUser().getName());
                waitingDTO.setMajor(waiting.getUser().getMajor());
                waitingDTO.setContent(waiting.getContent());
                waitingList.add(waitingDTO);
            }
        }

        List<ManagementDTO> teamList = new ArrayList<>();
        List<Team> allTeams = teamService.findTeamsByBoard(board);
        if(!allTeams.isEmpty())
        {
            for(Team team : allTeams)
            {
                if(team.getUser().getId() == user.getId() || !team.getIsValid()) continue;
                ManagementDTO teamDTO = new ManagementDTO();
                teamDTO.setTeamId(team.getId());
                teamDTO.setName(team.getUser().getName());
                teamDTO.setTel(team.getUser().getTel());
                teamDTO.setMajor(team.getUser().getMajor());

                teamList.add(teamDTO);
            }
        }

        model.addAttribute("waitingList", waitingList);
        model.addAttribute("teamList", teamList);
        model.addAttribute("currentMenu", "one");
        return "learning_core/management";
    }

    @PostMapping("/approve/{id}")
    public String waitingApprove(@PathVariable long id, Model model)
    {
        Optional<Waiting> findWaiting = waitingService.findByID(id);
        if(findWaiting.isEmpty()) return "redirect:/learning_core/mentor";

        Waiting waiting = findWaiting.get();
        teamService.save(waiting.getBoard(), waiting.getUser());
        waitingService.delete(waiting);

        model.addAttribute("boardID", waiting.getBoard().getId());
        model.addAttribute("currentMenu", "one");
        return "redirect:/learning_core/mentor/manage/" + waiting.getBoard().getId();
    }

    @PostMapping("/reject/{id}")
    public String teamReject(@PathVariable long id, Model model)
    {
        Optional<Waiting> findWaiting = waitingService.findByID(id);
        if(findWaiting.isEmpty()) return "redirect:/learning_core/mentor";

        Waiting waiting = findWaiting.get();
        waitingService.rejectWaiting(waiting);

        model.addAttribute("currentMenu", "one");
        return "redirect:/learning_core/mentor/manage/" + waiting.getBoard().getId();
    }

    @PostMapping("/expulsion/{id}")
    public String teamExpulsion(@PathVariable long id, Model model)
    {
        Optional<Team> findTeam = teamService.findById(id);
        if(findTeam.isEmpty()) return "redirect:/learning_core/mentor";

        Team team = findTeam.get();
        teamService.expulsionTeam(team);

        model.addAttribute("currentMenu", "one");
        return "redirect:/learning_core/mentor/manage/" + team.getBoard().getId();
    }
}
