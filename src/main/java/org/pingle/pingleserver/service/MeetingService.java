package org.pingle.pingleserver.service;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.UserMeeting;
import org.pingle.pingleserver.domain.enums.MRole;
import org.pingle.pingleserver.dto.request.MeetingRequest;
import org.pingle.pingleserver.dto.response.MyPingleResponse;
import org.pingle.pingleserver.dto.response.ParticipantsResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;
import org.pingle.pingleserver.repository.MeetingRepository;
import org.pingle.pingleserver.repository.UserMeetingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserMeetingRepository userMeetingRepository;

    @Transactional
    public Meeting createMeeting(MeetingRequest request, Pin pin) {
        return meetingRepository.save(
                Meeting.builder()
                        .pin(pin)
                        .category(request.category())
                        .name(request.name())
                        .maxParticipants(request.maxParticipants())
                        .chatLink(request.chatLink())
                        .startAt(request.startAt())
                        .endAt(request.endAt())
                        .build());
    }

    public ParticipantsResponse getParticipants(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ErrorMessage.RESOURCE_NOT_FOUND));
        List<UserMeeting> userMeetings = userMeetingRepository.findAllByMeeting(meeting);
        return ParticipantsResponse.of(userMeetings);
    }

    public List<MyPingleResponse> getMyPingles(Long userId, Long teamId, boolean participation) {
        List<Meeting> myMeetings = new ArrayList<>();
        if(participation) // 참여 완려 -> 이미 시작 endAt 이 현재보다 빠름
            myMeetings =  meetingRepository.findParticipatedMeetingsForUsersInTeamOrderByTime(userId, teamId, LocalDateTime.now());
        if(!participation) // 참여하지 않은 것 == 나중에 일어날 것 -> endAt이 현재보다 늦음
            myMeetings = meetingRepository.findUnparticipatedMeetingsForUsersInTeamOrderByTime(userId, teamId, LocalDateTime.now());
        return myMeetings.stream()
                .map(meeting -> MyPingleResponse.of(meeting, getOwnerName(meeting), isOwner(userId, meeting.getId()))).toList();
    }

    public String getOwnerName(Meeting meeting){
        Optional<UserMeeting> userMeeting = userMeetingRepository.findByMeetingAndMeetingRole(meeting, MRole.OWNER);
        if (userMeeting.isPresent()) {
            return userMeeting.get().getUser().getValidName();
        } else {
            return "(알 수 없음)";
        }
    }
    
    private boolean isOwner(Long userId, Long meetingId) {
        return userMeetingRepository.existsByUserIdAndMeetingIdAndMeetingRole(userId, meetingId, MRole.OWNER);
    }
  
    @Transactional
    public void deleteMeeting(Long userId, Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new CustomException(ErrorMessage.RESOURCE_NOT_FOUND));
        UserMeeting userMeeting = userMeetingRepository.findByUserIdAndMeeting(userId, meeting).orElseThrow(() -> new CustomException(ErrorMessage.RESOURCE_NOT_FOUND));
        if(userMeeting.getMeetingRole().getValue().equals("participants"))
            throw new CustomException(ErrorMessage.PERMISSION_DENIED);
        meetingRepository.delete(meeting);
    }
}

