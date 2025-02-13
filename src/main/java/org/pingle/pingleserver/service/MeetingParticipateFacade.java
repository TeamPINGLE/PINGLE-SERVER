package org.pingle.pingleserver.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetingParticipateFacade {
    private final UserMeetingService userMeetingService;
    private final LockManager lockManager;

    public void participateWithLock(Long userId, Long meetingId) {
        lockManager.executeWithLock(meetingId.toString(),
                () -> userMeetingService.participateMeeting(userId, meetingId));
    }
}
