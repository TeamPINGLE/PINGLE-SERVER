package org.pingle.pingleserver.oauth.dto;

public record AppleTokenResponse (
        String access_token,
        String token_type,
        String expires_in,
        String refresh_token,
        String id_token
){
}
