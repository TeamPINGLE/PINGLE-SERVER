package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
