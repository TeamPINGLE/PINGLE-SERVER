package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.domain.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {

    Optional<UserTeam> findByUserAndTeam(User user, Team team);
}
