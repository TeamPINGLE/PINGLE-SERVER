package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
}