package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
