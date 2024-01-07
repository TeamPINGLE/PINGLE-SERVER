package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.dto.response.TeamDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByNameIgnoreCaseContainingOrderByName(String name);

    @Query("SELECT new org.pingle.pingleserver.dto.response.TeamDetailDto(t, COUNT(m), COUNT(ut)) " +
            "FROM Team t " +
            "LEFT JOIN Pin p ON p.team = t " +
            "LEFT JOIN Meeting m ON m.pin = p " +
            "LEFT JOIN UserTeam ut ON ut.team = t " +
            "WHERE t.id = :teamId " +
            "GROUP BY t")
    Optional<TeamDetailDto> findTeamDetailsWithCounts(Long teamId);
}
