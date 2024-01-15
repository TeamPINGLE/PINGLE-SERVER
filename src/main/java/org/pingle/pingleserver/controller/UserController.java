package org.pingle.pingleserver.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.annotation.GUserId;
import org.pingle.pingleserver.annotation.UserId;
import org.pingle.pingleserver.constant.Constants;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.response.MyPingleResponse;
import org.pingle.pingleserver.dto.response.UserInfoResponse;
import org.pingle.pingleserver.dto.type.SuccessMessage;
import org.pingle.pingleserver.service.MeetingService;
import org.pingle.pingleserver.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final MeetingService meetingService;
    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> getLoginUserInfo(@UserId Long userId){
        return ApiResponse.success(SuccessMessage.OK, userService.getUserInfo(userId));
    }

    //1. 유저 id를 통해 유저 번개 리스트를 받는다.
    //2. 각각의 유저 번개 리스트에 대해 번개를 찾아가느 쿼리가 나간다.
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
}
