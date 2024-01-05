package org.pingle.pingleserver.oauth.service;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.oauth.dto.KakaoUserDto;
import org.pingle.pingleserver.oauth.dto.SocialInfoDto;
import org.pingle.pingleserver.oauth.client.KakaoFeignClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {
    private final KakaoFeignClient kakaoFeignClient;
    public SocialInfoDto getInfo(String providerToken) {
        KakaoUserDto kakaoUserdto = kakaoFeignClient.getUserInformation("Bearer " + providerToken);
        return SocialInfoDto.of(
                kakaoUserdto.id().toString(),
                kakaoUserdto.kakaoAccount().kakaoUserProfile().email(),
                kakaoUserdto.kakaoAccount().kakaoUserProfile().nickname());
    }
}
