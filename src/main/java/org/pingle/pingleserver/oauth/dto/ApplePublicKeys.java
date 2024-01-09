package org.pingle.pingleserver.oauth.dto;

import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;

import java.util.List;

public record ApplePublicKeys(List<ApplePublicKey> keys) {
    public ApplePublicKey getMatchesKey(String alg, String kid) {
        return this.keys
                .stream()
                .filter(k -> k.alg().equals(alg) && k.kid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorMessage.INVALID_APPLE_PUBLIC_KEY));
    }
}
