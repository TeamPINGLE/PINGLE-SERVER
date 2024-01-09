package org.pingle.pingleserver.controller;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.annotation.UserId;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.type.SuccessMessage;
import org.pingle.pingleserver.service.UserMeetingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private static final String GROUP_ID = "Group-Id";

    private final UserMeetingService userMeetingService;

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