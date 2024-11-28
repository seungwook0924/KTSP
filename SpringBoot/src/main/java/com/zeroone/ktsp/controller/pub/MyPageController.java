package com.zeroone.ktsp.controller.pub;

import com.zeroone.ktsp.DTO.mypage.MyTeamDTO;
import com.zeroone.ktsp.DTO.mypage.MyWaitingDTO;
import com.zeroone.ktsp.DTO.mypage.TeamMemberDTO;
import com.zeroone.ktsp.DTO.mypage.UpdateUserDTO;
import com.zeroone.ktsp.domain.Team;
import com.zeroone.ktsp.domain.User;
import com.zeroone.ktsp.domain.Waiting;
import com.zeroone.ktsp.enumeration.BoardType;
import com.zeroone.ktsp.service.TeamService;
import com.zeroone.ktsp.service.UserService;
import com.zeroone.ktsp.service.WaitingService;
import com.zeroone.ktsp.util.MethodUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MethodUtil methodUtil;
    private final WaitingService waitingService;

    private static final Pattern PHONE_PATTERN = Pattern.compile("^010-\\d{4}-\\d{4}$");
    private final TeamService teamService;

    @GetMapping("/update")
    public String showMypage(Model model, UpdateUserDTO updateUserDTO, HttpSession session)
    {
        User user = methodUtil.getSessionUser(session);

        model.addAttribute("user", user);
        updateUserDTO.setLevel(user.getLevel());
        updateUserDTO.setTel(user.getTel());
        updateUserDTO.setMajor(user.getMajor());
        updateUserDTO.setLastGrades(user.getLastGrades().toString());

        model.addAttribute("updateUserDTO", updateUserDTO);
        model.addAttribute("currentMenu", "one");
        return "my_page/update";
    }

    @PostMapping("/update")
    public String updateUser(@Validated @ModelAttribute UpdateUserDTO updateUserDTO, RedirectAttributes redirectAttributes, HttpSession session)
    {
        // 입력 데이터 유효성 검사
        String validationError = validateUserData(updateUserDTO);
        if (validationError != null)
        {
            redirectAttributes.addFlashAttribute("errorMessage", validationError);
            return "redirect:/mypage/update";
        }

        User user = methodUtil.getSessionUser(session);
        User updateUser = user.toBuilder()
                .level(updateUserDTO.getLevel())
                .tel(updateUserDTO.getTel())
                .major(updateUserDTO.getMajor())
                .lastGrades(new BigDecimal(updateUserDTO.getLastGrades()))
                .build();

        // 업데이트된 사용자 정보 저장
        userService.save(updateUser);
        session.setAttribute("user", updateUser);

        log.info("사용자 정보 업데이트 - 이름 : {} / 학번 : {}", user.getName(), user.getStudentNumber());
        return "redirect:/mypage";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@ModelAttribute UpdateUserDTO updateUserDTO, RedirectAttributes redirectAttributes, HttpSession session)
    {
        User user = methodUtil.getSessionUser(session);

        if (!updateUserDTO.getNewPassword().equals(updateUserDTO.getConfirmPassword()))
        {
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "redirect:/mypage";
        }

        String encodedPassword = passwordEncoder.encode(updateUserDTO.getNewPassword()); // 암호화

        User updateUser = user.toBuilder()
                .password(encodedPassword)
                .build();

        userService.save(updateUser);
        session.setAttribute("user", updateUser);

        log.info("비밀번호 변경 - 이름 : {} / 학번 : {}", user.getName(), user.getStudentNumber());
        return "redirect:/mypage";
    }

    @GetMapping("/myTeam")
    public String showMyTeams(Model model, HttpSession session)
    {
        User user = methodUtil.getSessionUser(session);
        List<Team> teams = teamService.findTeamsByUser(user);

        List<MyTeamDTO> myTeamDTOList = new ArrayList<>();
        if(!teams.isEmpty())
        {
            for(Team team : teams)
            {
                if((team.getBoard().getType().equals(BoardType.report)) || team.getBoard().getType().equals(BoardType.notice)) continue; // 불편 신고 및 공지사항은 전달하지 않음
                MyTeamDTO newDto = new MyTeamDTO();
                newDto.setId(team.getId());
                newDto.setWriter(team.getBoard().getUser().getName());
                newDto.setBoardId(team.getBoard().getId());
                newDto.setBoardName(team.getBoard().getTitle());
                newDto.setBoardType(methodUtil.convertBoardTypeToString(team.getBoard().getType()));
                newDto.setIsValid(team.getIsValid() ? "소속됨" : "추방됨");
                myTeamDTOList.add(newDto);
            }
        }

        List<Waiting> waitings = waitingService.findAllByUser(user);
        List<MyWaitingDTO> myWaitingDTOList = new ArrayList<>();
        if(!waitings.isEmpty())
        {
            for(Waiting waiting : waitings)
            {
                MyWaitingDTO newDto = new MyWaitingDTO();
                newDto.setBoardId(waiting.getBoard().getId());
                newDto.setWriter(waiting.getBoard().getUser().getName());
                newDto.setBoardType(methodUtil.convertBoardTypeToString(waiting.getBoard().getType()));
                newDto.setBoardName(waiting.getBoard().getTitle());
                newDto.setIsValid(waiting.getIsValid() ? "승인 대기중" : "거절됨");
                myWaitingDTOList.add(newDto);
            }
        }

        model.addAttribute("myWaitingDTOList", myWaitingDTOList);
        model.addAttribute("myTeamDTOList", myTeamDTOList);
        model.addAttribute("currentMenu", "two");
        return "my_page/my_team";
    }

    @GetMapping("myTeam/{id}")
    public String showTeamView(@PathVariable long id, Model model, RedirectAttributes redirectAttributes, HttpSession session)
    {
        User user = methodUtil.getSessionUser(session);
        Optional<Team> findTeam = teamService.findById(id);
        if(findTeam.isEmpty())
        {
            redirectAttributes.addFlashAttribute("errorMessage", "게시글이 삭제되었습니다.");
            return "redirect:/mypage/myTeam";
        }

        Team team = findTeam.get();

        List<Team> members = teamService.findTeamsByBoard(team.getBoard());
        List<TeamMemberDTO> teamMemberDTOList = new ArrayList<>();
        if(!members.isEmpty())
        {
            boolean flag = true;
            for(Team member : members)
            {
                if(!member.getIsValid()) continue;
                TeamMemberDTO newDto = new TeamMemberDTO();
                newDto.setName(member.getUser().getName());
                newDto.setTel(member.getUser().getTel());
                newDto.setLevel(methodUtil.convertUserLevelToString(member.getUser().getLevel()));
                newDto.setMajor(member.getUser().getMajor());
                teamMemberDTOList.add(newDto);
                if(member.getUser().getId() == user.getId()) flag = false;
            }
            if(flag) return "redirect:/mypage"; //팀원 소속이 아니라면 리다이렉트
        }

        model.addAttribute("teamMemberDTOList", teamMemberDTOList);
        model.addAttribute("currentMenu", "two");
        return "my_page/team_view";
    }

    private String validateUserData(UpdateUserDTO updateUserDTO)
    {
        // 전공 필드 유효성 검사
        if (updateUserDTO.getMajor() == null || updateUserDTO.getMajor().trim().isEmpty()) return "전공을 입력해주세요.";
        if (updateUserDTO.getMajor().length() < 2 || updateUserDTO.getMajor().length() > 20) return "전공은 2글자 이상 20글자 이하로 입력해야 합니다.";

        // 전화번호 형식 검사
        if (!PHONE_PATTERN.matcher(updateUserDTO.getTel()).matches()) return "전화번호는 010-XXXX-XXXX 형식이어야 합니다.";

        // 학점 유효성 검사
        try
        {
            BigDecimal grades = new BigDecimal(updateUserDTO.getLastGrades());

            if (grades.scale() > 2) return "학점은 소수점 아래 두 자리까지만 입력 가능합니다.";

            BigDecimal minGrade = new BigDecimal("0.0");
            BigDecimal maxGrade = new BigDecimal("4.5");

            if (grades.compareTo(minGrade) < 0 || grades.compareTo(maxGrade) > 0) return "학점은 0.0 이상 4.5 이하여야 합니다.";
        }
        catch (NumberFormatException e)
        {
            return "학점은 숫자 형식이어야 합니다.";
        }

        return null;
    }


}