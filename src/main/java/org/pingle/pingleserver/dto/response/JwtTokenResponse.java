package org.pingle.pingleserver.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record JwtTokenResponse (@NotNull String accessToken, @NotNull String refreshToken){
    public static JwtTokenResponse of(String accessToken, String refreshToken) {
        return JwtTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
