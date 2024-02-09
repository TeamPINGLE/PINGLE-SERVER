package org.pingle.pingleserver.service;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.domain.UserTeam;
import org.pingle.pingleserver.domain.enums.TRole;
import org.pingle.pingleserver.dto.reponse.TeamCreationResponse;
import org.pingle.pingleserver.dto.reponse.TeamNameDuplicatedResponse;
import org.pingle.pingleserver.dto.request.TeamCreationRequest;
import org.pingle.pingleserver.dto.request.TeamRegisterRequest;
import org.pingle.pingleserver.dto.response.SelectedTeamResponse;
import org.pingle.pingleserver.dto.response.TeamDetailDto;
import org.pingle.pingleserver.dto.response.TeamRegisterResponse;
import org.pingle.pingleserver.dto.response.TeamSearchResultResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;
import org.pingle.pingleserver.repository.TeamRepository;
import org.pingle.pingleserver.repository.UserRepository;
import org.pingle.pingleserver.repository.UserTeamRepository;
import org.pingle.pingleserver.utils.CustomSearchUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Random;

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
                .orElseThrow(() -> new CustomException(ErrorMessage.RESOURCE_NOT_FOUND));
        return SelectedTeamResponse.of(team.team(), team.meetingCount(), team.participantCount());
    }

    @Transactional
    public TeamRegisterResponse registerTeam(Long userId, Long teamId, TeamRegisterRequest request) {
        Team team = teamRepository.findByIdOrThrow(teamId);
        if (!team.getCode().equals(request.code())) {
            throw new CustomException(ErrorMessage.INVALID_GROUP_CODE);
        }
        User user = userRepository.findByIdOrThrow(userId);
        boolean isRegistered = userTeamRepository.existsByUserAndTeam(user, team);
        if (isRegistered) {
            throw new CustomException(ErrorMessage.ALREADY_REGISTERED_USER);
        }
        UserTeam newUserTeam = UserTeam.builder()
                .user(user)
                .team(team)
                .teamRole(TRole.PARTICIPANT)
                .build();
        userTeamRepository.save(newUserTeam);
        return TeamRegisterResponse.of(team.getId(), team.getName());
    }

    public TeamNameDuplicatedResponse checkTeamName(String name) {
        return new TeamNameDuplicatedResponse(!teamRepository.existsByNameIgnoreCase(name));
    }

    @Transactional
    public TeamCreationResponse createTeam(Long userId, TeamCreationRequest request) {
        User user = userRepository.findByIdOrThrow(userId);

        String code = generateCode();
        Team team = Team.builder()
                .name(request.name())
                .email(request.email())
                .keyword(request.keyword())
                .code(code)
                .build();
        teamRepository.save(team);

        UserTeam userTeam = UserTeam.builder()
                .user(user)
                .team(team)
                .teamRole(TRole.OWNER)
                .build();
        userTeamRepository.save(userTeam);

        return TeamCreationResponse.of(team);
    }

    private String generateCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder(12);
        Random random = new Random();

        for (int i = 0; i < 12; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }
}
