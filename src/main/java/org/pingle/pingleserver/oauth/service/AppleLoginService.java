package org.pingle.pingleserver.oauth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.oauth.dto.ApplePublicKeys;
import org.pingle.pingleserver.oauth.dto.SocialInfoDto;
import org.pingle.pingleserver.oauth.client.AppleFeignClient;
import org.pingle.pingleserver.oauth.verify.AppleJwtParser;
import org.pingle.pingleserver.oauth.verify.PublicKeyGenerator;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppleLoginService {

    private final AppleFeignClient appleFeignClient;
    private final AppleJwtParser appleJwtParser;
    private final PublicKeyGenerator publicKeyGenerator;

    public SocialInfoDto getInfo(String identityToken, String name) {
        Map<String, String> headers = appleJwtParser.parseHeaders(identityToken);
        ApplePublicKeys applePublicKeys = appleFeignClient.getApplePublicKeys();
        PublicKey publicKey = publicKeyGenerator.generatePublicKey(headers, applePublicKeys);
        Claims claims = appleJwtParser.parsePublicKeyAndGetClaims(identityToken, publicKey);
        return SocialInfoDto.of(claims.get("sub", String.class), claims.get("email", String.class), name);
    }

}