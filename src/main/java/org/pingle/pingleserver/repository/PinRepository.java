package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.domain.Point;
import org.pingle.pingleserver.domain.enums.MCategory;
import org.pingle.pingleserver.dto.reponse.PinResponse;
import org.pingle.pingleserver.dto.response.RankingIndividualResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long> {
    boolean existsByPointAndTeam(Point point, Team team);
    Pin findByPointAndTeam(Point point, Team team);
    @Query("select distinct new org.pingle.pingleserver.dto.reponse.PinResponse(p.id, p.point.x, p.point.y, m.category) from Pin p join p.meetingList m " +
            "WHERE p.team.id = :teamId " +
            "AND m.startAt > CURRENT_TIMESTAMP " +
            "AND (m.category = :category OR :category IS NULL) " +
            "AND (p.address.address LIKE concat('%' ,COALESCE(:q, ''), '%') OR p.name LIKE concat('%' ,COALESCE(:q, ''), '%')) " +
            "AND m.startAt = (SELECT MIN(m.startAt) FROM Meeting m WHERE m.pin = p and m.startAt > CURRENT_TIMESTAMP AND (m.category = :category OR :category IS NULL)) ")
    List<PinResponse> findPinsByTeamIdAndCategoryAndQ(Long teamId, MCategory category, String q);
    @Query("select new org.pingle.pingleserver.dto.response.RankingIndividualResponse(p.name, max(m.endAt), count(p)) " +
            "from Meeting m join m.pin p " +
            "WHERE m.endAt < CURRENT_TIMESTAMP AND p.team.id = :teamId " +
            "group by p " +
            "order by count(p) desc, max(m.endAt) desc")
    List<RankingIndividualResponse> findPinsWithMeetingsBeforeCurrentTimestampAndTeamId(Long teamId);
    // todo: use native query to solve n+1 problem
//    @Query(value = "SELECT p.*, COUNT(m.id) FROM pin p LEFT JOIN meeting m ON p.id = m.pin_id WHERE p.team_id = :teamId AND m.start_at > CURRENT_TIMESTAMP AND m.category = :category GROUP BY p.id", nativeQuery = true)
//    List<PinResponse> test(Long teamId, MCategory category);
}
