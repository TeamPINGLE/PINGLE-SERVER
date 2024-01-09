package org.pingle.pingleserver.service;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.domain.Address;
import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.Point;
import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.dto.request.MeetingRequest;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.BusinessException;
import org.pingle.pingleserver.repository.PinRepository;
import org.pingle.pingleserver.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PinService {
    private final PinRepository pinRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public Pin verifyAndReturnPin(MeetingRequest request, Long groupId) {
        Team team = teamRepository.findById(groupId).orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_RESOURCE));
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

    private boolean exist(Point point) {
        return pinRepository.existsByPoint(point);
    }
}
