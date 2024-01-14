package org.pingle.pingleserver.service;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.UserMeeting;
import org.pingle.pingleserver.dto.request.MeetingRequest;
import org.pingle.pingleserver.dto.response.ParticipantsResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;
import org.pingle.pingleserver.repository.MeetingRepository;
import org.pingle.pingleserver.repository.UserMeetingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
