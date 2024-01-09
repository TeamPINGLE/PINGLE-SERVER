package org.pingle.pingleserver.controller;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.annotation.UserId;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.response.UserInfoResponse;
import org.pingle.pingleserver.dto.type.SuccessMessage;
import org.pingle.pingleserver.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> getLoginUserInfo(@UserId Long userId){
        return ApiResponse.success(SuccessMessage.OK, userService.getUserInfo(userId));
    }

    @DeleteMapping("/leave")
    public ApiResponse<Void> leave(@UserId Long userId){
        userService.leave(userId);
        return ApiResponse.success(SuccessMessage.OK);
    }
}
