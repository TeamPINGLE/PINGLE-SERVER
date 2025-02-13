package org.pingle.pingleserver.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.UserMeeting;
import org.pingle.pingleserver.domain.enums.MCategory;
import org.pingle.pingleserver.domain.enums.MRole;
import org.pingle.pingleserver.domain.enums.SearchOrder;
import org.pingle.pingleserver.dto.request.MeetingRequest;
import org.pingle.pingleserver.dto.response.MyPingleResponse;
import org.pingle.pingleserver.dto.response.ParticipantsResponse;
import org.pingle.pingleserver.dto.response.SearchIndividualResponse;
import org.pingle.pingleserver.dto.response.SearchResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;
import org.pingle.pingleserver.repository.MeetingRepository;
import org.pingle.pingleserver.repository.TeamRepository;
import org.pingle.pingleserver.repository.UserMeetingRepository;
import org.pingle.pingleserver.utils.SlackUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingService {

    private final SlackUtil slackUtil;
    private final MeetingRepository meetingRepository;
    private final UserMeetingRepository userMeetingRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public Meeting createMeeting(MeetingRequest request, Pin pin) {
        Meeting savedMeeting = meetingRepository.save(
                Meeting.builder()
                        .pin(pin)
                        .category(request.category())
                        .name(request.name())
                        .maxParticipants(request.maxParticipants())
                        .chatLink(request.chatLink())
                        .startAt(request.startAt())
                        .endAt(request.endAt())
                        .build());
        slackUtil.alertCreateMeeting(request.location(), request.name());
        return savedMeeting;
    }

    public ParticipantsResponse getParticipants(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ErrorMessage.RESOURCE_NOT_FOUND));
        List<UserMeeting> userMeetings = userMeetingRepository.findAllByMeeting(meeting);
        return ParticipantsResponse.of(userMeetings);
    }

    public List<MyPingleResponse> getMyPingles(Long userId, Long teamId, boolean participation) {
        List<Meeting> myMeetings = new ArrayList<>();
        if (participation) // 참여 완려 -> 이미 시작 endAt 이 현재보다 빠름
            myMeetings = meetingRepository.findParticipatedMeetingsForUsersInTeamOrderByTime(userId, teamId, LocalDateTime.now());
        if (!participation) // 참여하지 않은 것 == 나중에 일어날 것 -> endAt이 현재보다 늦음
            myMeetings = meetingRepository.findUnparticipatedMeetingsForUsersInTeamOrderByTime(userId, teamId, LocalDateTime.now());
        return myMeetings.stream()
                .map(meeting -> MyPingleResponse.of(meeting, getOwnerName(meeting), isOwner(userId, meeting.getId()))).toList();
    }

    public String getOwnerName(Meeting meeting) {
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
        if (userMeeting.getMeetingRole().getValue().equals("participants"))
            throw new CustomException(ErrorMessage.PERMISSION_DENIED);
        meetingRepository.delete(meeting);
    }

    public SearchResponse searchMeetings(Long userId, String q, MCategory category, Long teamId, SearchOrder order) {

        if (q!=null && q.isBlank()) throw new CustomException(ErrorMessage.BAD_REQUEST);
        if (!teamRepository.existsById(teamId)) throw new CustomException(ErrorMessage.RESOURCE_NOT_FOUND);

        if (order.getValue().equals(SearchOrder.NEW.getValue())) {
            List<SearchIndividualResponse> responses = meetingRepository.findMeetingsByParameterOOrderByCreatedAt(q, category ,teamId)
                    .stream().map(meeting -> SearchIndividualResponse.of(meeting, userId)).collect(Collectors.toList());
            return SearchResponse.of(responses);
        }

        if (order.getValue().equals(SearchOrder.UPCOMING.getValue())) {
            List<SearchIndividualResponse> responses = meetingRepository.findMeetingsByParameterOOrderByStartAt(q, category, teamId)
                    .stream().map(meeting -> SearchIndividualResponse.of(meeting, userId)).collect(Collectors.toList());
            return SearchResponse.of(responses);
        }
        return null;
    }
}

