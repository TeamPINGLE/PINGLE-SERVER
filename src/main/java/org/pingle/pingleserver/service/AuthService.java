package org.pingle.pingleserver.service;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.domain.enums.Provider;
import org.pingle.pingleserver.domain.enums.URole;
import org.pingle.pingleserver.dto.request.ReissueRequest;
import org.pingle.pingleserver.oauth.dto.SocialInfoDto;
import org.pingle.pingleserver.dto.request.LoginRequest;
import org.pingle.pingleserver.dto.response.JwtTokenResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.BusinessException;
import org.pingle.pingleserver.oauth.service.AppleLoginService;
import org.pingle.pingleserver.oauth.service.KakaoLoginService;
import org.pingle.pingleserver.repository.UserRepository;
import org.pingle.pingleserver.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final JwtUtil jwtUtil;
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
    public JwtTokenResponse reissue(ReissueRequest request) {
        jwtUtil.getTokenBody(request.refreshToken());
        User user = userRepository.findByRefreshTokenAndIsDeleted(request.refreshToken(), false)
                .orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        return generateTokensWithUpdateRefreshToken(user);
    }

    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        user.updateRefreshToken(null);
    }

    private SocialInfoDto getSocialInfo(LoginRequest request, String providerToken){
        if (request.provider().toString().equals(Provider.APPLE.toString())){
            return appleLoginService.getInfo(providerToken, request.name());
        } else if (request.provider().toString().equals(Provider.KAKAO.toString())){
            return kakaoLoginService.getInfo(providerToken);
        } else {
            throw new BusinessException(ErrorMessage.INVALID_PROVIDER_ERROR);
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
        }

        return userRepository.findByProviderAndSerialIdAndIsDeleted(provider, socialInfo.serialId(), false)
                .orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
    }

    private JwtTokenResponse generateTokensWithUpdateRefreshToken(User user){
        JwtTokenResponse jwtTokenResponse = jwtUtil.generateTokens(user.getId(), user.getRole());
        user.updateRefreshToken(jwtTokenResponse.refreshToken());
        return jwtTokenResponse;
    }

}
