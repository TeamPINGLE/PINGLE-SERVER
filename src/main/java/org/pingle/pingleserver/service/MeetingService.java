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

    //유저 갖고와서 검증하고
    //team도 있는지 검증하고
    //usermeetings에서 유저가 포함된 유저 번개 전부 갖고옴
    //각각의 유저번개의 번개 전부 갖고옴-> 리스트로 만들고 이때 핀의 Location도 갖고옴
    public List<MyPingleResponse> getMyPingles(Long userId, Long teamId, boolean participation) {
        List<Meeting> myMeetings = new ArrayList<>();
        if(participation) // 참여 완려 -> 이미 시작 startAt이 현재보다
            myMeetings =  meetingRepository.findParticipatedMeetingsForUsersInTeamOrderByTime(userId, teamId, LocalDateTime.now());
        if(!participation) // 참여하지 않은 것 == 나중에 일어날 것 -> startat이 현재보다 늦음
            myMeetings = meetingRepository.findUnparticipatedMeetingsForUsersInTeamOrderByTime(userId, teamId, LocalDateTime.now());

        return myMeetings.stream()
                .map(meeting -> MyPingleResponse.of(meeting, getOwnerName(meeting), isOwner(userId, meeting.getId()))).toList();

    }
    private String getOwnerName(Meeting meeting) {
        UserMeeting userMeeting = userMeetingRepository.findByMeetingAndMeetingRole(meeting, MRole.OWNER)
                .orElseThrow(() ->new CustomException(ErrorMessage.RESOURCE_NOT_FOUND));
        return userMeeting.getUser().getName();
    }
    private boolean isOwner(Long userId, Long meetingId) {
        return userMeetingRepository.existsByUserIdAndMeetingIdAndMeetingRole(userId, meetingId, MRole.OWNER);
    }
}
