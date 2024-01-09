package org.pingle.pingleserver.service;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.domain.UserMeeting;
import org.pingle.pingleserver.domain.enums.MRole;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.BusinessException;
import org.pingle.pingleserver.repository.MeetingRepository;
import org.pingle.pingleserver.repository.UserMeetingRepository;
import org.pingle.pingleserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserMeetingService {

    private final UserMeetingRepository userMeetingRepository;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long participateMeeting(Long userId, Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_RESOURCE));
        if(isParticipating(userId, meeting))
            throw new BusinessException(ErrorMessage.RESOURCE_CONFLICT);
        if((getCurParticipants(meeting)) >= meeting.getMaxParticipants())
            throw new BusinessException(ErrorMessage.RESOURCE_CONFLICT);
        User user = userRepository.findById(userId).orElseThrow(() ->new BusinessException(ErrorMessage.NO_SUCH_USER));
        return userMeetingRepository.save(new UserMeeting(user, meeting, MRole.PARTICIPANTS)).getId();

    }

    @Transactional
    public Long cancelMeeting(Long userId, Long meetingId) {
        UserMeeting userMeeting = userMeetingRepository.findByUserIdAndMeetingId(userId, meetingId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_RESOURCE));
        userMeetingRepository.delete(userMeeting);
        return userMeeting.getId();
    }

    private int getCurParticipants(Meeting meeting) {
        return userMeetingRepository.findAllByMeeting(meeting).size();
    }

    private boolean isParticipating(Long userId, Meeting meeting) {
        return userMeetingRepository.existsByUserIdAndMeeting(userId, meeting);
    }

}
