package org.pingle.pingleserver.constant;

public class Constants {
    public static final String USER_ID_CLAIM_NAME = "uid";
    public static final String USER_ROLE_CLAIM_NAME = "rol";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TEAM_ID = "X-Team-Id";
    public static final String PROVIDER_TOKEN_HEADER = "X-Provider-Token";
    public static final String APPLE_LOGOUT_HEADER = "X-Apple-Code";
    public static final String APPLE_KEY_ID_NAME = "kid";
    public static final String APPLE_ISSUER_NAME = "iss";
    public static final String APPLE_AUDIENCE_NAME = "aud";
    public static final String APPLE_SUBJECT_NAME = "sub";
    public static final String[] AUTH_WHITELIST = {
            "/v1/auth/login",
            "/v1/auth/reissue",
            "/actuator/health",
            "/test/**",
            "/qr/**",

            "/v3/api-docs.html",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
    };

}
