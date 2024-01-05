package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.UserMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMeetingRepository extends JpaRepository<UserMeeting, Long> {
}
