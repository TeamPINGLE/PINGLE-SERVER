package org.pingle.pingleserver.service;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.domain.UserMeeting;
import org.pingle.pingleserver.domain.enums.MRole;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;
import org.pingle.pingleserver.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserMeetingService {
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserMeetingRepository userMeetingRepository;
    private final UserTeamRepository userTeamRepository;
    private final MeetingRepository meetingRepository;

    @Transactional
    public Long addOwnerToMeeting(Long userId, Meeting meeting) {
        User user = userRepository.findByIdOrThrow(userId);
        return userMeetingRepository.save(
                UserMeeting.builder()
                        .user(user)
                        .meeting(meeting)
                        .meetingRole(MRole.OWNER)
                        .build()).getId();
    }

    //유저가 그룹에 있는지
    public void verifyUser(Long userId, Long groupId) {
        User user = userRepository.findByIdOrThrow(userId);
        Team team = teamRepository.findByIdOrThrow(groupId);
        userTeamRepository.findByUserAndTeam(user, team)
                .orElseThrow(() -> new CustomException(ErrorMessage.GROUP_PERMISSION_DENIED));
    }

    @Transactional
    public Long participateMeeting(Long userId, Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new CustomException(ErrorMessage.MEETING_NOT_FOUND));
        if(isParticipating(userId, meeting))
            throw new CustomException(ErrorMessage.RESOURCE_CONFLICT);
        if((getCurParticipants(meeting)) >= meeting.getMaxParticipants())
            throw new CustomException(ErrorMessage.RESOURCE_CONFLICT);
        User user = userRepository.findByIdOrThrow(userId);
        return userMeetingRepository.save(new UserMeeting(user, meeting, MRole.PARTICIPANTS)).getId();
    }

    @Transactional
    public Long cancelMeeting(Long userId, Long meetingId) {
        UserMeeting userMeeting = userMeetingRepository.findByUserIdAndMeetingId(userId, meetingId)
                .orElseThrow(() -> new CustomException(ErrorMessage.USERMEETING_NOT_FOUND));
        if(userMeetingRepository.existsByUserIdAndMeetingIdAndMeetingRole(userId, meetingId, MRole.OWNER))
            throw new CustomException(ErrorMessage.BAD_REQUEST);
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
