package org.pingle.pingleserver.oauth.dto;

public record ApplePublicKey(
        String kty, String kid, String use, String alg, String n, String e, String email
) {

}
