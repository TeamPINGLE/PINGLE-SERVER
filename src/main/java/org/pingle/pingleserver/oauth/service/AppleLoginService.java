package org.pingle.pingleserver.oauth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pingle.pingleserver.constant.Constants;
import org.pingle.pingleserver.oauth.dto.ApplePublicKeys;
import org.pingle.pingleserver.oauth.dto.AppleTokenResponse;
import org.pingle.pingleserver.oauth.dto.SocialInfoDto;
import org.pingle.pingleserver.oauth.client.AppleFeignClient;
import org.pingle.pingleserver.oauth.verify.AppleJwtParser;
import org.pingle.pingleserver.oauth.verify.PublicKeyGenerator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleLoginService implements InitializingBean {

    @Value("${apple.private-key}")
    private String applePrivateKey;

    @Value("${apple.key-id}")
    private String appleKeyId;

    @Value("${apple.issuer}")
    private String appleIssuer;

    @Value("${apple.audience}")
    private String appleAudience;

    @Value("${apple.client-id}")
    private String appleClientId;

    private Key appleKey;

    private final RestTemplate restTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(applePrivateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("EC");
        this.appleKey = kf.generatePrivate(spec);
    }

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

    public void revoke(String code) {
        String clientSecret = createClientSecret();
        String token = getAppleToken(clientSecret, code);
        requestRevoke(clientSecret, token);
    }

    private String getAppleToken(String clientSecret, String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", appleClientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", "authorization_code");
        map.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        String url = "https://appleid.apple.com/auth/token";
        ResponseEntity<AppleTokenResponse> response = restTemplate.postForEntity(url, request, AppleTokenResponse.class);

        return response.getBody().refresh_token();
    }

    private void requestRevoke(String clientSecret, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", appleClientId);
        map.add("client_secret", clientSecret);
        map.add("token", token);
        map.add("token_type_hint", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        String url = "https://appleid.apple.com/auth/revoke";
        restTemplate.postForEntity(url, request, String.class);
    }

    private String createClientSecret() {
        Claims claims = Jwts.claims();
        claims.put(Constants.APPLE_ISSUER_NAME, appleIssuer);
        claims.put(Constants.APPLE_AUDIENCE_NAME, appleAudience);
        claims.put(Constants.APPLE_SUBJECT_NAME, appleClientId);
        return Jwts.builder()
                .setHeaderParam(Constants.APPLE_KEY_ID_NAME, appleKeyId)
                .setHeaderParam("alg", SignatureAlgorithm.ES256)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) // 5분
                .signWith(appleKey, SignatureAlgorithm.ES256)
                .compact();
    }
}