package org.pingle.pingleserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.pingle.pingleserver.annotation.GUserId;
import org.pingle.pingleserver.annotation.UserId;
import org.pingle.pingleserver.constant.Constants;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.response.MyPingleResponse;
import org.pingle.pingleserver.dto.response.UserInfoResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "User", description = "사용자 정보")
public interface UserApi {

    @Operation(summary = "로그인한 사용자 정보 조회")
    ApiResponse<UserInfoResponse> getLoginUserInfo(@UserId Long userId);

    @Operation(summary = "내가 참여한 미팅 조회")
    ApiResponse<List<MyPingleResponse>> getMyPingles (
            @GUserId Long userId, @NotNull @RequestParam boolean participation,
            @RequestHeader(Constants.TEAM_ID)Long teamId);

    @Operation(summary = "회원 탈퇴")
    ApiResponse<Void> leave(@UserId Long userId, @RequestHeader(Constants.APPLE_LOGOUT_HEADER) String code);
}
