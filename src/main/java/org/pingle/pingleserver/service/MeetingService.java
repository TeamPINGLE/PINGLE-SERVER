package org.pingle.pingleserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.dto.request.MeetingRequest;
import org.pingle.pingleserver.repository.MeetingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingService {


    private final MeetingRepository meetingRepository;

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
}
