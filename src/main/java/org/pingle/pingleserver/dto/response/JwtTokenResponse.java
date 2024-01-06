package org.pingle.pingleserver.dto.response;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record JwtTokenResponse (@NonNull String accessToken, @NonNull String refreshToken){
    public static JwtTokenResponse of(String accessToken, String refreshToken) {
        return JwtTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
