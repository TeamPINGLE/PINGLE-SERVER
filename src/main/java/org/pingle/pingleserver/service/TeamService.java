package org.pingle.pingleserver.service;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.dto.response.TeamSearchResultResponse;
import org.pingle.pingleserver.repository.TeamRepository;
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
    private final CustomSearchUtil customSearchUtil;

    public List<TeamSearchResultResponse> searchTeams(String teamName) {
        List<Team> teams = teamRepository.findAllByNameIgnoreCaseContainingOrderByName(teamName);
        return teams.stream()
                .sorted(Comparator.comparingInt((Team team) -> customSearchUtil.calculateScoreIgnoreCase(team.getName(), teamName)))
                .map(TeamSearchResultResponse::of)
                .collect(Collectors.toList());
    }
}
