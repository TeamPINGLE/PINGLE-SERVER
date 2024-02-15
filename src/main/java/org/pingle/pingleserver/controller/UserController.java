package org.pingle.pingleserver.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.annotation.GUserId;
import org.pingle.pingleserver.annotation.UserId;
import org.pingle.pingleserver.constant.Constants;
import org.pingle.pingleserver.controller.swagger.UserApi;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.response.MyPingleResponse;
import org.pingle.pingleserver.dto.response.MyTeamResponse;
import org.pingle.pingleserver.dto.response.UserInfoResponse;
import org.pingle.pingleserver.dto.type.SuccessMessage;
import org.pingle.pingleserver.service.MeetingService;
import org.pingle.pingleserver.service.TeamService;
import org.pingle.pingleserver.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final MeetingService meetingService;
    private final UserService userService;
    private final TeamService teamService;

    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> getLoginUserInfo(@UserId Long userId){
        return ApiResponse.success(SuccessMessage.OK, userService.getUserInfo(userId));
    }

    @GetMapping("/me/meetings")
    public ApiResponse<List<MyPingleResponse>> getMyPingles (@GUserId Long userId, @NotNull @RequestParam boolean participation,
                                                             @RequestHeader(Constants.TEAM_ID)Long teamId) {
        return ApiResponse.success(SuccessMessage.OK, meetingService.getMyPingles(userId, teamId, participation));
    }

    @DeleteMapping("/leave")
    public ApiResponse<Void> leave(@UserId Long userId, @Nullable @RequestHeader(Constants.APPLE_LOGOUT_HEADER) String code){
        userService.leave(userId, code);
        return ApiResponse.success(SuccessMessage.OK);
    }

    @GetMapping("/me/teams")
    public ApiResponse<List<MyTeamResponse>> getMyTeams(@UserId Long userId) {
        return ApiResponse.success(SuccessMessage.OK, teamService.getMyTeams(userId));
    }
}
