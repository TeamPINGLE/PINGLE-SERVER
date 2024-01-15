package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query("SELECT DISTINCT m FROM Meeting m " +//미팅 갖고 오는데
            "JOIN FETCH m.pin p " +//핀 조인
            "JOIN FETCH p.team t " +//팀 조인
            "JOIN UserMeeting um ON um.meeting = m " +//
            "WHERE um.user.id = :userId " +//유저 미팅 중에서 주어진 userid만 넣음
            "   AND (:teamId IS NULL OR t.id = :teamId) " + // 팀에서 팀만 넣음
            "   AND m.startAt > :currentDateTime " + //예정된 것들만
            "ORDER BY m.startAt ASC") // 가까운 순으로
    List<Meeting> findUnparticipatedMeetingsForUsersInTeamOrderByTime(Long userId, Long teamId, @Param("currentDateTime")LocalDateTime currentDateTime);
    @Query("SELECT DISTINCT m FROM Meeting m " +//미팅 갖고 오는데
            "JOIN FETCH m.pin p " +//핀 조인
            "JOIN FETCH p.team t " +//팀 조인
            "JOIN UserMeeting um ON um.meeting = m " +//
            "WHERE um.user.id = :userId " +//유저 미팅 중에서 주어진 userid만 넣음
            "   AND (:teamId IS NULL OR t.id = :teamId) " + // 팀에서 팀만 넣음
            "   AND m.startAt <= :currentDateTime " + //참여완료
            "ORDER BY m.startAt DESC") // 가까운 순으로
    List<Meeting> findParticipatedMeetingsForUsersInTeamOrderByTime(Long userId, Long teamId, @Param("currentDateTime")LocalDateTime currentDateTime);
}
