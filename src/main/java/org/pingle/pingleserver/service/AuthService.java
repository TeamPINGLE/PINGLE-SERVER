package org.pingle.pingleserver.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.constant.Constants;
import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.domain.enums.Provider;
import org.pingle.pingleserver.domain.enums.URole;
import org.pingle.pingleserver.exception.CustomException;
import org.pingle.pingleserver.oauth.dto.SocialInfoDto;
import org.pingle.pingleserver.dto.request.LoginRequest;
import org.pingle.pingleserver.dto.response.JwtTokenResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.oauth.service.AppleLoginService;
import org.pingle.pingleserver.oauth.service.KakaoLoginService;
import org.pingle.pingleserver.repository.UserRepository;
import org.pingle.pingleserver.utils.JwtUtil;
import org.pingle.pingleserver.utils.SlackUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final JwtUtil jwtUtil;
    private final SlackUtil slackUtil;
    private final AppleLoginService appleLoginService;
    private final KakaoLoginService kakaoLoginService;
    private final UserRepository userRepository;

    @Transactional
    public JwtTokenResponse login(String providerToken, LoginRequest request) {
        SocialInfoDto socialInfo = getSocialInfo(request, providerToken);
        User user = loadOrCreateUser(request.provider(), socialInfo);
        return generateTokensWithUpdateRefreshToken(user);
    }

    @Transactional
    public JwtTokenResponse reissue(String token) {
        String refreshToken = getToken(token);
        Claims claims = jwtUtil.getTokenBody(refreshToken);
        if (claims.get(Constants.USER_ROLE_CLAIM_NAME, String.class) != null) {
            throw new CustomException(ErrorMessage.INVALID_TOKEN_TYPE);
        }
        Long userId = claims.get(Constants.USER_ID_CLAIM_NAME, Long.class);
        User user = userRepository.findByIdAndIsDeletedOrThrow(userId, false);
        if (!user.getRefreshToken().equals(refreshToken)){
            throw new CustomException(ErrorMessage.INVALID_JWT);
        }
        return generateTokensWithUpdateRefreshToken(user);
    }

    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findByIdOrThrow(userId);
        user.updateRefreshToken(null);
    }

    private SocialInfoDto getSocialInfo(LoginRequest request, String providerToken){
        if (request.provider().toString().equals(Provider.APPLE.toString())){
            return appleLoginService.getInfo(providerToken, request.name());
        } else if (request.provider().toString().equals(Provider.KAKAO.toString())){
            return kakaoLoginService.getInfo(providerToken);
        } else {
            throw new CustomException(ErrorMessage.INVALID_PROVIDER_ERROR);
        }
    }

    private User loadOrCreateUser(Provider provider, SocialInfoDto socialInfo){
        boolean isRegistered = userRepository.existsByProviderAndSerialIdAndIsDeleted(provider, socialInfo.serialId(), false);

        if (!isRegistered){
            User newUser = User.builder()
                    .provider(provider)
                    .serialId(socialInfo.serialId())
                    .email(socialInfo.email())
                    .name(socialInfo.name())
                    .role(URole.USER)
                    .build();
            userRepository.save(newUser);
            slackUtil.alertUserSignUp(newUser.getName(), newUser.getEmail());
        }

        return userRepository.findByProviderAndSerialIdAndIsDeleted(provider, socialInfo.serialId(), false)
                .orElseThrow(() -> new CustomException(ErrorMessage.USER_NOT_FOUND));
    }

    private JwtTokenResponse generateTokensWithUpdateRefreshToken(User user){
        JwtTokenResponse jwtTokenResponse = jwtUtil.generateTokens(user.getId(), user.getRole());
        user.updateRefreshToken(jwtTokenResponse.refreshToken());
        return jwtTokenResponse;
    }

    private String getToken(String token){
        if (token.startsWith(Constants.BEARER_PREFIX)){
            return token.substring(Constants.BEARER_PREFIX.length());
        } else {
            return token;
        }
    }

}
