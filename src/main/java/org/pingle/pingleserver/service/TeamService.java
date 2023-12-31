package org.pingle.pingleserver.service;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.domain.UserTeam;
import org.pingle.pingleserver.domain.enums.TRole;
import org.pingle.pingleserver.dto.request.TeamRegisterRequest;
import org.pingle.pingleserver.dto.response.SelectedTeamResponse;
import org.pingle.pingleserver.dto.response.TeamDetailDto;
import org.pingle.pingleserver.dto.response.TeamRegistResponse;
import org.pingle.pingleserver.dto.response.TeamSearchResultResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.BusinessException;
import org.pingle.pingleserver.repository.TeamRepository;
import org.pingle.pingleserver.repository.UserRepository;
import org.pingle.pingleserver.repository.UserTeamRepository;
import org.pingle.pingleserver.utils.CustomSearchUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;
    private final CustomSearchUtil customSearchUtil;

    public List<TeamSearchResultResponse> searchTeams(String teamName) {
        List<Team> teams = teamRepository.findAllByNameIgnoreCaseContainingOrderByName(teamName);
        return teams.stream()
                .sorted(Comparator.comparingInt((Team team) -> customSearchUtil.calculateScoreIgnoreCase(team.getName(), teamName)))
                .map(TeamSearchResultResponse::of)
                .collect(Collectors.toList());
    }

    public SelectedTeamResponse getTeam(Long teamId) {
        TeamDetailDto team = teamRepository.findTeamDetailsWithCounts(teamId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_RESOURCE));
        return SelectedTeamResponse.of(team.team(), team.meetingCount(), team.participantCount());
    }

    @Transactional
    public TeamRegistResponse registTeam(Long userId, Long teamId, TeamRegisterRequest request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_RESOURCE));
        if (!team.getCode().equals(request.code())) {
            throw new BusinessException(ErrorMessage.INVALID_GROUP_CODE);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.NO_SUCH_USER));
        boolean isRegistered = userTeamRepository.existsByUserAndTeam(user, team);
        if (isRegistered) {
            throw new BusinessException(ErrorMessage.ALREADY_REGISTERED_USER);
        }
        UserTeam newUserTeam = UserTeam.builder()
                .user(user)
                .team(team)
                .teamRole(TRole.PARTICIPANT)
                .build();
        userTeamRepository.save(newUserTeam);
        return TeamRegistResponse.of(team.getId(), team.getName());
    }
}
