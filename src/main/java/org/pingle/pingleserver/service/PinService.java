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
import org.pingle.pingleserver.repository.*;
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
    private final UserMeetingRepository userMeetingRepository;

    public List<PinResponse> getPinsFilterByCategory(Long teamId, MCategory category) {
        Team team = teamRepository.findByIdOrThrow(teamId);
        List<Pin> pinList = pinRepository.findAllByTeam(team);

        if(category == null) return pinList.stream().filter(pin -> !allTimeNotValid(pin))
                .map(PinResponse::ofWithNoFilter).toList();
        return pinList.stream().filter(pin -> !allTimeNotValid(pin))//시간
                .filter(pin -> checkMeetingsCategoryOfPin(pin, category))//카테고리 미팅 하나도 없다면 pass
                .map(pin -> PinResponse.ofWithFilter(pin, category)).toList();//시간 유효, 카테고리 미팅 포함x -> pass
    }

    public List<MeetingResponse> getMeetingsDetail(Long userId, Long pinId, MCategory category) {
        Pin pin = pinRepository.findById(pinId).orElseThrow(() -> new CustomException(ErrorMessage.RESOURCE_NOT_FOUND));
        Comparator<Meeting> comparator = Comparator.comparing(Meeting::getStartAt);//핀내의 미팅 시간순으로 정렬
        List<Meeting> meetings = pin.getMeetingList();
        meetings.sort(comparator);//핀의 모든 미팅을 시간순으로 정렬, 이제 미팅중에서 현재 시간보다 이른 startat이른 것 버려야돼
        List<Meeting> filteredMeetings = meetings.stream().filter(this::isValidMeetingTime).toList();
        List<MeetingResponse> responseList = new ArrayList<>();
        if(category == null) {
            for (Meeting meeting : filteredMeetings) {
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
        for (Meeting meeting : filteredMeetings) {
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
        if(!exist(new Point(request.x(), request.y()))) {
             return pinRepository.save(Pin.builder()
                    .address(new Address(request.roadAddress(), request.address()))
                    .name(request.location())
                    .point(new Point(request.x(), request.y()))
                    .team(team)
                    .build());
        }
        return pinRepository.findByPoint(new Point(request.x(), request.y()));
    }
  
    private boolean checkMeetingsCategoryOfPin(Pin pin, MCategory category) {
        List<Meeting> meetingList = pin.getMeetingList();
        for(Meeting meeting : meetingList) {
            if(meeting.getCategory().getValue().equals(category.getValue()))
                return true;
        }
        return false;
    }

    private boolean allTimeNotValid(Pin pin) {
        int count = 0;
        List<Meeting> meetingList = pin.getMeetingList();
        for (Meeting meeting : meetingList) {
            if (meeting.getStartAt().isBefore(LocalDateTime.now()))
                count++;
        }
        if (count == meetingList.size())
            return true;
        return false;
    }

    private boolean isValidMeetingTime(Meeting meeting) {
        if (meeting.getStartAt().isBefore(LocalDateTime.now()))
            return false;
        return true;
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
        if(userMeetingRepository.existsByUserIdAndMeetingIdAndMeetingRole(userId, meetingId, MRole.OWNER))
            return true;
        return false;
    }

    private boolean exist(Point point) {
        return pinRepository.existsByPoint(point);
    }
}
