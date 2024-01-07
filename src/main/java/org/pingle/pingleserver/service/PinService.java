package org.pingle.pingleserver.service;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.domain.enums.MCategory;
import org.pingle.pingleserver.dto.reponse.PinResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.BusinessException;
import org.pingle.pingleserver.repository.PinRepository;
import org.pingle.pingleserver.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PinService {

    private final PinRepository pinRepository;
    private final TeamRepository teamRepository;

    public List<PinResponse> getPinListFilterByCategory(Long teamId, MCategory category) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_RESOURCE));
        List<Pin> pinList = pinRepository.findAllByTeam(team);//현재 약속중 category갖는 핀만 갖고 있음
        if(category == null) return pinList.stream().map(PinResponse::of).toList();
        return pinList.stream().filter(pin -> checkMeetingsCategoryOfPin(pin, category)).map(PinResponse::of).toList();
    }

    private boolean checkMeetingsCategoryOfPin(Pin pin, MCategory category) {
        List<Meeting> meetingList = pin.getMeetingList();
        for(Meeting meeting : meetingList) {
            if(meeting.getCategory().getValue().equals(category.getValue()))
                return true;
        }
        return false;
    }
}
