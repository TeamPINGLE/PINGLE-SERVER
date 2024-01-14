package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.enums.MCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query("SELECT COUNT(m) FROM Meeting m WHERE m.pin.id = :pinId AND m.startAt > CURRENT_TIMESTAMP AND m.category = :category")
    int countMeetingsForPin(Long pinId, MCategory category);

    List<Meeting> findByPinIdAndCategoryAndStartAtAfter(Long pinId, MCategory category, LocalDateTime currentTime);

    List<Meeting> findByPinIdAndStartAtAfter(Long pinId, LocalDateTime currentTime);
}
