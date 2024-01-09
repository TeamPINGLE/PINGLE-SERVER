package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long> {
    List<Pin> findAllByTeam(Team team);
    boolean existsByPoint(Point point);
    Pin findByPoint(Point point);
}
