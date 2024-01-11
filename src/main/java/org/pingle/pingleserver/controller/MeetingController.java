package org.pingle.pingleserver.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.annotation.UserId;
import org.pingle.pingleserver.annotation.GUserId;
import org.pingle.pingleserver.constant.Constants;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.request.MeetingRequest;
import org.pingle.pingleserver.dto.type.SuccessMessage;
import org.pingle.pingleserver.service.MeetingService;
import org.pingle.pingleserver.service.PinService;

import org.pingle.pingleserver.service.UserMeetingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final UserMeetingService userMeetingService;
    private final PinService pinService;

    @PostMapping
    public ApiResponse<?> createMeeting(@Valid @RequestBody MeetingRequest request, @GUserId Long userId,
                                        @RequestHeader(Constants.GROUP_ID) Long groupId) {
        Pin pin = pinService.verifyAndReturnPin(request, groupId);//핀 없으면 핀 생성 후 반환, 있다면 핀 생성
        Meeting meeting = meetingService.createMeeting(request, pin);//번개 생성
        Long userMeetingId = userMeetingService.addOwnerToMeeting(userId, meeting);

        return ApiResponse.success(SuccessMessage.CREATED);
    }

    @PostMapping("/{meetingId}/join")
    public ApiResponse<?> participateMeeting (@UserId Long userId, @PathVariable("meetingId") Long meetingId) {
        Long userMeetingId = userMeetingService.participateMeeting(userId, meetingId);
        return ApiResponse.success(SuccessMessage.CREATED);
    }

    @DeleteMapping("/{meetingId}/cancel")
    public ApiResponse<?> cancelMeeting (@UserId Long userId, @PathVariable("meetingId") Long meetingId) {
        Long cancelledId = userMeetingService.cancelMeeting(userId, meetingId);
        return ApiResponse.success(SuccessMessage.OK);
    }
}
