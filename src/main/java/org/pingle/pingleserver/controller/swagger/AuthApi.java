package org.pingle.pingleserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.pingle.pingleserver.annotation.UserId;
import org.pingle.pingleserver.constant.Constants;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.request.LoginRequest;
import org.pingle.pingleserver.dto.response.JwtTokenResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Auth", description = "유저 인증 관련 API")
public interface AuthApi {

    @Operation(summary = "로그인", description = "로그인한다.")
    ApiResponse<JwtTokenResponse> login(
            @NotNull @RequestHeader(Constants.PROVIDER_TOKEN_HEADER) String providerToken,
            @Valid @RequestBody LoginRequest request);

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급한다.")
    ApiResponse<JwtTokenResponse> reissue(
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER) String refreshToken);

    @Operation(summary = "로그아웃", description = "로그아웃한다.")
    ApiResponse<?> logout(Long userId);

}
