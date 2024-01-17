package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.domain.Point;
import org.pingle.pingleserver.domain.enums.MCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long> {
    List<Pin> findAllByTeam(Team team);
    boolean existsByPointAndTeam(Point point, Team team);
    Pin findByPointAndTeam(Point point, Team team);
    @Query("SELECT DISTINCT p FROM Pin p WHERE p.team.id = :teamId AND p.id IN (SELECT m.pin.id FROM Meeting m WHERE m.startAt > CURRENT_TIMESTAMP AND m.category = :category)")
    List<Pin> findPinsWithCategoryAndTimeBefore(Long teamId, MCategory category);
    @Query("SELECT DISTINCT p FROM Pin p WHERE p.team.id = :teamId AND p.id IN (SELECT m.pin.id FROM Meeting m WHERE m.startAt > CURRENT_TIMESTAMP)")
    List<Pin> findPinsAndTimeBefore(Long teamId);

    // todo: use native query to solve n+1 problem
//    @Query(value = "SELECT p.*, COUNT(m.id) FROM pin p LEFT JOIN meeting m ON p.id = m.pin_id WHERE p.team_id = :teamId AND m.start_at > CURRENT_TIMESTAMP AND m.category = :category GROUP BY p.id", nativeQuery = true)
//    List<PinResponse> test(Long teamId, MCategory category);
}
