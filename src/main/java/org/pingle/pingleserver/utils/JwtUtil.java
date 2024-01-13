package org.pingle.pingleserver.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.pingle.pingleserver.constant.Constants;
import org.pingle.pingleserver.domain.enums.URole;
import org.pingle.pingleserver.dto.response.JwtTokenResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil implements InitializingBean {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-token-expire-period}")
    private Integer accessTokenExpirePeriod;
    @Value("${jwt.refresh-token-expire-period}")
    @Getter
    private Integer refreshTokenExpirePeriod;

    private Key key;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtTokenResponse generateTokens(Long id, URole role) {
        return JwtTokenResponse.of(
                generateToken(id, role, accessTokenExpirePeriod),
                generateToken(id, null, refreshTokenExpirePeriod));
    }

    public Claims getTokenBody(String token){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException ex) {
            throw new CustomException(ErrorMessage.INVALID_JWT);
        } catch (ExpiredJwtException ex) {
            throw new CustomException(ErrorMessage.EXPIRED_JWT);
        } catch (UnsupportedJwtException ex) {
            throw new CustomException(ErrorMessage.UNSUPPORTED_JWT);
        } catch (IllegalArgumentException ex) {
            throw new CustomException(ErrorMessage.JWT_IS_EMPTY);
        }
    }

    private String generateToken(Long id, URole role, Integer expirePeriod) {
        Claims claims = Jwts.claims();
        claims.put(Constants.USER_ID_CLAIM_NAME, id);
        if (role != null)
            claims.put(Constants.USER_ROLE_CLAIM_NAME, role);

        return Jwts.builder()
                .setHeaderParam(Header.JWT_TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirePeriod))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
