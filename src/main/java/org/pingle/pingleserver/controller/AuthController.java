package org.pingle.pingleserver.controller;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.request.LoginRequest;
import org.pingle.pingleserver.dto.response.JwtTokenResponse;
import org.pingle.pingleserver.dto.type.SuccessMessage;
import org.pingle.pingleserver.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<JwtTokenResponse> login(
            @RequestHeader("Provider-Token") String providerToken,
            @RequestBody LoginRequest request){
        return ApiResponse.success(SuccessMessage.OK, authService.login(providerToken, request));
    }
}
