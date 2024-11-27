package com.zeroone.ktsp.controller;

import com.zeroone.ktsp.DTO.JoinTeamDTO;
import com.zeroone.ktsp.DTO.ManagementDTO;
import com.zeroone.ktsp.domain.Board;
import com.zeroone.ktsp.domain.Team;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.domain.Waiting;
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

    @GetMapping("/manage/{id}")
    public String showManageView(@PathVariable long id, HttpSession session, Model model, @RequestParam String sidebarType, @RequestParam String boardType)
    {
        User user = (User) session.getAttribute("user");
        Optional<Board> findBoard = boardService.findById(id);

        if(findBoard.isEmpty()) return "redirect:/" + boardType; //게시글이 존재하지 않을 때 리다이렉트
        Board board = findBoard.get();
        if(user.getId() != board.getUser().getId()) return "redirect:/" + boardType; //본인이 아닌 다른 사람이 접근했을 때 리다이렉트

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
        model.addAttribute("id", id);
        model.addAttribute("sidebarType", sidebarType);
        model.addAttribute("currentMenu", boardType);
        return "team_management";
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

        if (waitingService.existsByBoardAndUser(board, user)) return ResponseEntity.badRequest().body("이미 지원하셨습니다. 지원 내역 및 결과는 마이페이지에서 확인할 수 있습니다."); // 이미 지원했는지 확인

        waitingService.save(board, user, content);
        return ResponseEntity.ok("지원이 성공적으로 완료되었습니다.");
    }

    @PostMapping("/approve/{id}")
    public String waitingApprove(@PathVariable long id, Model model, @RequestParam String sidebarType, @RequestParam String boardType)
    {
        Optional<Waiting> findWaiting = waitingService.findByID(id);
        if(findWaiting.isEmpty()) return "redirect:/team/manage/" + id;

        Waiting waiting = findWaiting.get();
        teamService.save(waiting.getBoard(), waiting.getUser());
        waitingService.delete(waiting);

        model.addAttribute("boardID", waiting.getBoard().getId());
        model.addAttribute("sidebarType", sidebarType);
        model.addAttribute("currentMenu", boardType);
        return "redirect:/team/manage/" + waiting.getBoard().getId() + "?sidebarType=" + sidebarType + "&boardType=" + boardType;
    }

    @PostMapping("/reject/{id}")
    public String teamReject(@PathVariable long id, Model model, @RequestParam String sidebarType, @RequestParam String boardType)
    {
        Optional<Waiting> findWaiting = waitingService.findByID(id);
        if(findWaiting.isEmpty()) return "redirect:/" + boardType;

        Waiting waiting = findWaiting.get();
        waitingService.rejectWaiting(waiting);
        log.info("거절됨 : {} / id : {}", waiting.getUser().getName(), id);

        model.addAttribute("sidebarType", sidebarType);
        model.addAttribute("currentMenu", boardType);
        return "redirect:/team/manage/" + waiting.getBoard().getId() + "?sidebarType=" + sidebarType + "&boardType=" + boardType;
    }

    @PostMapping("/expulsion/{id}")
    public String teamExpulsion(@PathVariable long id, Model model, @RequestParam String sidebarType, @RequestParam String boardType)
    {
        Optional<Team> findTeam = teamService.findById(id);
        if(findTeam.isEmpty()) return "redirect:/" + boardType;

        Team team = findTeam.get();
        teamService.expulsionTeam(team);

        model.addAttribute("sidebarType", sidebarType);
        model.addAttribute("currentMenu", boardType);
        return "redirect:/team/manage/" + team.getBoard().getId() + "?sidebarType=" + sidebarType + "&boardType=" + boardType;
    }
}
