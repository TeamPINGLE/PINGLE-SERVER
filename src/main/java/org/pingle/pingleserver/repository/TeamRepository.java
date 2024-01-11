package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.dto.response.TeamDetailDto;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    default Team findByIdOrThrow(Long teamId) {
        return findById(teamId)
                .orElseThrow((() -> new CustomException(ErrorMessage.RESOURCE_NOT_FOUND)));
    }
    List<Team> findAllByNameIgnoreCaseContainingOrderByName(String name);

    @Query("SELECT new org.pingle.pingleserver.dto.response.TeamDetailDto(t, COUNT(DISTINCT m.id), COUNT(DISTINCT ut.id)) " +
            "FROM Team t " +
            "LEFT JOIN Pin p ON p.team = t " +
            "LEFT JOIN Meeting m ON m.pin = p " +
            "LEFT JOIN UserTeam ut ON ut.team = t " +
            "WHERE t.id = :teamId " +
            "GROUP BY t")
    Optional<TeamDetailDto> findTeamDetailsWithCounts(Long teamId);
}
