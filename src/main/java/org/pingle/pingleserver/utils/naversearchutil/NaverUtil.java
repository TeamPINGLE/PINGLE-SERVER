package org.pingle.pingleserver.utils.naversearchutil;


import org.pingle.pingleserver.dto.reponse.LocationResponse;
import org.pingle.pingleserver.dto.request.NaverLocationRequest;
import org.pingle.pingleserver.dto.reponse.NaverLocationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NaverUtil {

    @Value("${naver.client-id}")
    private String naverClientId;

    @Value("${naver.client-secret}")
    private String naverSecret;

    @Value("${naver.location-search-url}")
    private String naverLocationSearchUrl;

    public List<LocationResponse> getLocationInfo(String location) {

        NaverLocationResponse naverLocationResponse = locationSearch(NaverLocationRequest.of(location));

        return convertResponse(naverLocationResponse);
    }

    private NaverLocationResponse locationSearch(NaverLocationRequest request) {
        URI uri = UriComponentsBuilder
                .fromUriString(naverLocationSearchUrl)
                .queryParams(request.toMap())
                .build()
                .encode()
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-Naver-Client-Id", naverClientId);
        httpHeaders.set("X-Naver-Client-Secret", naverSecret);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
        ParameterizedTypeReference<NaverLocationResponse> responseType = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<NaverLocationResponse> responseEntity = new RestTemplate()
                .exchange(uri, HttpMethod.GET, httpEntity, responseType);

        return responseEntity.getBody();
    }

    private List<LocationResponse> convertResponse(NaverLocationResponse response) {

        List<NaverLocationResponse.SearchLocationItem> items = response.items();
        List<LocationResponse> responseList = items
                .stream()
                .map(item -> new LocationResponse(convertX(item.getMapx()), convertY(item.getMapy()), trim(item.getTitle()), trim(item.getAddress()), trim(item.getRoadAddress())))
                .collect(Collectors.toList());

        return responseList;
    }
    private double convertX(int x) {

        int integerPart = x / 10000000;
        int decimalPart = x % 10000000;

        double decimalPartInDouble = decimalPart / 10000000.0;

        return integerPart + decimalPartInDouble;
    }

    private double convertY(int x) {

        int integerPart = x / 10000000;
        int decimalPart = x % 10000000;

        double decimalPartInDouble = decimalPart / 10000000.0;

        return integerPart + decimalPartInDouble;
    }

    private String trim(String string) {

        return string.replaceAll("<[^>]*>", "");

    }

}

