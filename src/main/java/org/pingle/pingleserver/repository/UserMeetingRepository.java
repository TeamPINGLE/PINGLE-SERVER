package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.UserMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMeetingRepository extends JpaRepository<UserMeeting, Long> {
    List<UserMeeting> findAllByMeeting(Meeting meeting);
    boolean existsByUserIdAndMeeting(Long userId, Meeting meeting);
    Optional<UserMeeting> findByUserIdAndMeetingId(Long userId, Long MeetingId);
}
