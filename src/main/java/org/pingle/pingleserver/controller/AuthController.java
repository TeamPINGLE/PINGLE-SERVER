package org.pingle.pingleserver.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.annotation.UserId;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.request.LoginRequest;
import org.pingle.pingleserver.dto.request.ReissueRequest;
import org.pingle.pingleserver.dto.response.JwtTokenResponse;
import org.pingle.pingleserver.dto.type.SuccessMessage;
import org.pingle.pingleserver.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<JwtTokenResponse> login(
            @NotNull @RequestHeader("Provider-Token") String providerToken,
            @Valid @RequestBody LoginRequest request){
        return ApiResponse.success(SuccessMessage.OK, authService.login(providerToken, request));
    }

    @PostMapping("/reissue")
    public ApiResponse<JwtTokenResponse> reissue(
            @Valid @RequestBody ReissueRequest request){
        return ApiResponse.success(SuccessMessage.OK, authService.reissue(request));
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout(@UserId Long userId){
        authService.logout(userId);
        return ApiResponse.success(SuccessMessage.LOGOUT_SUCCESS);
    }
}
