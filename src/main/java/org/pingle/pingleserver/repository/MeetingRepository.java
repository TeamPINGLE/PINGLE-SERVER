package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.enums.MCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query("SELECT COUNT(m) FROM Meeting m WHERE m.pin.id = :pinId AND m.startAt > CURRENT_TIMESTAMP")
    int countMeetingsForPinWithoutCategory(Long pinId);

    @Query("SELECT COUNT(m) FROM Meeting m WHERE m.pin.id = :pinId AND m.startAt > CURRENT_TIMESTAMP AND m.category = :category")
    int countMeetingsForPinWithCategory(Long pinId, MCategory category);

    Optional<Meeting> findFirstByPinIdAndStartAtAfterOrderByStartAtAsc(Long pinId, LocalDateTime currentTime);

    List<Meeting> findByPinIdAndCategoryAndStartAtAfterOrderByStartAt(Long pinId, MCategory category, LocalDateTime currentTime);

    List<Meeting> findByPinIdAndStartAtAfterOrderByStartAt(Long pinId, LocalDateTime currentTime);
}
