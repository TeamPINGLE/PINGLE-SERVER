package org.pingle.pingleserver.oauth.client;

import org.pingle.pingleserver.oauth.dto.KakaoUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoFeignClient", url = "https://kapi.kakao.com")
public interface KakaoFeignClient {
    @GetMapping(value = "/v2/user/me")
    KakaoUserDto getUserInformation(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);
}