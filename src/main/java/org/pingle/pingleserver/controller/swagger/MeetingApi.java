package org.pingle.pingleserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.pingle.pingleserver.annotation.GUserId;
import org.pingle.pingleserver.constant.Constants;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.request.MeetingRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Meeting", description = "번개 API")
public interface MeetingApi {

    @Operation(summary = "번개 생성", description = "번개를 생성한다.")
    ApiResponse<?> createMeeting(@Valid @RequestBody MeetingRequest request, @GUserId Long userId,
                                 @RequestHeader(Constants.TEAM_ID) Long groupId);
    @Operation(summary = "번개 참여", description = "번개에 참여한다.")
    ApiResponse<?> participateMeeting (@GUserId Long userId, Long meetingId);

    @Operation(summary = "번개 취소", description = "번개 참여를 취소한다.")
    ApiResponse<?> cancelMeeting (@GUserId Long userId, Long meetingId);

    @Operation(summary = "번개 참여자 조회", description = "번개 참여자를 조회한다.")
    ApiResponse<?> getMeetingParticipants (Long meetingId);

    @Operation(summary = "번개 삭제", description = "번개를 삭제한다.")
    ApiResponse<?> deleteMeeting(@GUserId Long userId, Long meetingId);
}
