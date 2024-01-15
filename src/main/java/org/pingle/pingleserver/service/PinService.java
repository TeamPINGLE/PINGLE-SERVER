package org.pingle.pingleserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.domain.UserMeeting;
import org.pingle.pingleserver.domain.enums.MCategory;
import org.pingle.pingleserver.domain.enums.MRole;
import org.pingle.pingleserver.dto.reponse.MeetingResponse;
import org.pingle.pingleserver.dto.reponse.PinResponse;
import org.pingle.pingleserver.domain.Address;
import org.pingle.pingleserver.domain.Point;
import org.pingle.pingleserver.dto.request.MeetingRequest;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;
import org.pingle.pingleserver.repository.MeetingRepository;
import org.pingle.pingleserver.repository.PinRepository;
import org.pingle.pingleserver.repository.TeamRepository;
import org.pingle.pingleserver.repository.UserMeetingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PinService {

    private final PinRepository pinRepository;
    private final TeamRepository teamRepository;
    private final MeetingRepository meetingRepository;
    private final UserMeetingRepository userMeetingRepository;

    public List<PinResponse> getPinsFilterByCategory(Long teamId, MCategory category) {
        Team team = teamRepository.findByIdOrThrow(teamId);
        List<Pin> pinList = pinRepository.findAllByTeam(team);
        if(category == null) return pinList.stream().map(PinResponse::ofWithNoFilter).toList();
        return pinList.stream().filter(pin -> checkMeetingsCategoryOfPin(pin, category))
                .map(pin -> PinResponse.ofWithFilter(pin, category)).toList();
    }

    public List<MeetingResponse> getMeetingsDetail(Long userId, Long pinId, MCategory category) {
        Pin pin = pinRepository.findById(pinId).orElseThrow(() -> new CustomException(ErrorMessage.RESOURCE_NOT_FOUND));
        Comparator<Meeting> comparator = Comparator.comparing(Meeting::getStartAt);
        List<Meeting> meetingList = pin.getMeetings();
        meetingList.sort(comparator);//핀의 모든 미팅을 시간순으로 정렬
        List<MeetingResponse> responseList = new ArrayList<>();
        if(category == null) {
            for (Meeting meeting : meetingList) {
                responseList.add(MeetingResponse.builder()
                        .id(meeting.getId())
                        .category(meeting.getCategory())
                        .name(meeting.getName())
                        .ownerName(getOwnerName(meeting))//만든사람
                        .location(pin.getName())
                        .date(getDateFromDateTime(meeting.getStartAt()))//meeting start 날짜 parsing
                        .startAt(getTimeFromDateTime(meeting.getStartAt()))//start 시간 parsing
                        .endAt(getTimeFromDateTime(meeting.getEndAt()))//end 시간 parsing
                        .maxParticipants(meeting.getMaxParticipants())
                        .curParticipants(getCurParticipants(meeting))
                        .isParticipating(isParticipating(userId, meeting))
                        .chatLink(meeting.getChatLink())
                        .isOwner(isOwner(userId, meeting.getId()))
                        .build());
            }
            return responseList;
        }
        // 및 카테고리에 포함한 것만
        for (Meeting meeting : meetingList) {
            if(meeting.getCategory().getValue().equals(category.getValue())) {
                responseList.add(MeetingResponse.builder()
                        .id(meeting.getId())
                        .category(meeting.getCategory())
                        .name(meeting.getName())
                        .ownerName(getOwnerName(meeting))//만든사람
                        .location(pin.getName())
                        .date(getDateFromDateTime(meeting.getStartAt()))//meeting start 날짜 parsing
                        .startAt(getTimeFromDateTime(meeting.getStartAt()))//start 시간 parsing
                        .endAt(getTimeFromDateTime(meeting.getEndAt()))//end 시간 parsing
                        .maxParticipants(meeting.getMaxParticipants())
                        .curParticipants(getCurParticipants(meeting))
                        .isParticipating(isParticipating(userId, meeting))
                        .chatLink(meeting.getChatLink())
                        .isOwner(isOwner(userId, meeting.getId()))
                        .build());
            }
        }
        return responseList;

    }

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

    public List<PinResponse> getPins(Long teamId, MCategory category) {
        List<Pin> pins;

        if (category == null) {
            pins = pinRepository.findPinsAndTimeBefore(teamId);
            return pins.stream().map(pin -> {
                return PinResponse.of(
                        pin,
                        meetingRepository.findFirstByPinIdAndStartAtAfterOrderByStartAtAsc(pin.getId(), LocalDateTime.now())
                                .map(Meeting::getCategory).orElse(MCategory.OTHERS),
                        meetingRepository.countMeetingsForPinWithoutCategory(pin.getId()));
            }).toList();
        }

        pins = pinRepository.findPinsWithCategoryAndTimeBefore(teamId, category);
        return pins.stream()
                .map(pin -> PinResponse.of(pin, category, meetingRepository.countMeetingsForPinWithCategory(pin.getId(), category))).toList();
    }

    public List<MeetingResponse> getMeetings(Long pinId, Long userId, MCategory category){
        List<Meeting> meetings;

        if (category == null){
            meetings = meetingRepository.findByPinIdAndStartAtAfterOrderByStartAt(pinId, LocalDateTime.now());
        } else {
            meetings = meetingRepository.findByPinIdAndCategoryAndStartAtAfterOrderByStartAt(pinId, category, LocalDateTime.now());
        }
        return meetings.stream()
                .map(meeting -> MeetingResponse.of(meeting, getOwnerName(meeting), getCurParticipants(meeting),
                        isParticipating(userId, meeting), isOwner(userId, meeting.getId()))).toList();


    }
  
    private boolean checkMeetingsCategoryOfPin(Pin pin, MCategory category) {
        List<Meeting> meetingList = pin.getMeetings();
        for(Meeting meeting : meetingList) {
            if(meeting.getCategory().getValue().equals(category.getValue()))
                return true;
        }
        return false;
    }

    private String getOwnerName(Meeting meeting) {
        UserMeeting userMeeting = userMeetingRepository.findByMeetingAndMeetingRole(meeting, MRole.OWNER)
                .orElseThrow(() ->new CustomException(ErrorMessage.RESOURCE_NOT_FOUND));
        return userMeeting.getUser().getName();
    }

    private int getCurParticipants(Meeting meeting) {
        return userMeetingRepository.findAllByMeeting(meeting).size();
    }

    private boolean isParticipating(Long userId, Meeting meeting) {
        return userMeetingRepository.existsByUserIdAndMeeting(userId, meeting);
    }

    private String getDateFromDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(formatter);
    }
    private String getTimeFromDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return localDateTime.format(formatter);
    }

    private boolean isOwner(Long userId, Long meetingId) {
        return userMeetingRepository.existsByUserIdAndMeetingIdAndMeetingRole(userId, meetingId, MRole.OWNER);
    }
}
