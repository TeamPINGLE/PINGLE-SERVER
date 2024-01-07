package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByNameIgnoreCaseContainingOrderByName(String name);
}
