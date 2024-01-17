package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.UserMeeting;
import org.pingle.pingleserver.domain.enums.MRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserMeetingRepository extends JpaRepository<UserMeeting, Long> {
    Optional<UserMeeting> findByMeetingAndMeetingRole(Meeting meeting, MRole role);
    Optional<UserMeeting> findByUserIdAndMeeting(Long userId, Meeting meeting);
    List<UserMeeting> findAllByMeeting(Meeting meeting);
    boolean existsByUserIdAndMeeting(Long userId, Meeting meeting);
    Optional<UserMeeting> findByUserIdAndMeetingId(Long userId, Long MeetingId);
    boolean existsByUserIdAndMeetingIdAndMeetingRole(Long userId, Long meetingId, MRole role);
}
