package org.pingle.pingleserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.domain.enums.MCategory;
import org.pingle.pingleserver.domain.enums.MRole;
import org.pingle.pingleserver.dto.reponse.MeetingResponse;
import org.pingle.pingleserver.dto.reponse.PinResponse;
import org.pingle.pingleserver.domain.Address;
import org.pingle.pingleserver.domain.Point;
import org.pingle.pingleserver.dto.request.MeetingRequest;
import org.pingle.pingleserver.dto.response.RankingIndividualResponse;
import org.pingle.pingleserver.dto.response.RankingResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;
import org.pingle.pingleserver.repository.MeetingRepository;
import org.pingle.pingleserver.repository.PinRepository;
import org.pingle.pingleserver.repository.TeamRepository;
import org.pingle.pingleserver.repository.UserMeetingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PinService {

    private final MeetingService meetingService;
    private final PinRepository pinRepository;
    private final TeamRepository teamRepository;
    private final MeetingRepository meetingRepository;
    private final UserMeetingRepository userMeetingRepository;

    @Transactional
    public Pin verifyAndReturnPin(MeetingRequest request, Long groupId) {
        Team team = teamRepository.findByIdOrThrow(groupId);
        if(!pinRepository.existsByPointAndTeam(new Point(request.x(), request.y()), team)) {
             return pinRepository.save(Pin.builder()
                    .address(new Address(request.roadAddress(), request.address()))
                    .name(request.location())
                    .point(new Point(request.x(), request.y()))
                    .team(team)
                    .build());
        }
        return pinRepository.findByPointAndTeam(new Point(request.x(), request.y()), team);
    }

    public List<PinResponse> getPins(Long teamId, MCategory category, String q) {
        if (q!=null && q.isBlank()) throw new CustomException(ErrorMessage.BAD_REQUEST);
        if (!teamRepository.existsById(teamId)) throw new CustomException(ErrorMessage.RESOURCE_NOT_FOUND);
        return pinRepository.findPinsByTeamIdAndCategoryAndQ(teamId, category, q);
    }

    public List<MeetingResponse> getMeetings(Long pinId, Long userId, MCategory category){
        List<Meeting> meetings;

        if (category == null){
            meetings = meetingRepository.findByPinIdAndStartAtAfterOrderByStartAt(pinId, LocalDateTime.now());
        } else {
            meetings = meetingRepository.findByPinIdAndCategoryAndStartAtAfterOrderByStartAt(pinId, category, LocalDateTime.now());
        }
        return meetings.stream()
                .map(meeting -> MeetingResponse.of(meeting, meetingService.getOwnerName(meeting), getCurParticipants(meeting),
                        isParticipating(userId, meeting), isOwner(userId, meeting.getId()))).toList();
    }

    private int getCurParticipants(Meeting meeting) {
        return userMeetingRepository.findAllByMeeting(meeting).size();
    }

    private boolean isParticipating(Long userId, Meeting meeting) {
        return userMeetingRepository.existsByUserIdAndMeeting(userId, meeting);
    }

    private boolean isOwner(Long userId, Long meetingId) {
        return userMeetingRepository.existsByUserIdAndMeetingIdAndMeetingRole(userId, meetingId, MRole.OWNER);
    }

    public RankingResponse getRankings(Long teamId) {
        if(!teamRepository.existsById(teamId)) throw new CustomException(ErrorMessage.RESOURCE_NOT_FOUND);
        List<RankingIndividualResponse> response = pinRepository.findPinsWithMeetingsBeforeCurrentTimestampAndTeamId(teamId);
        return RankingResponse.of(response);
    }
}
