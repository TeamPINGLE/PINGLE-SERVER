package org.pingle.pingleserver.dto.request;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public record NaverLocationRequest(String query, int display, int start, String sort) {

    private static final int DISPLAY = 5;

    private static final int START = 1;

    private static final String SORT = "random";

    public static NaverLocationRequest of(String query) {
        return new NaverLocationRequest(query, DISPLAY, START, SORT);
    }

    public MultiValueMap<String, String> toMap() {
        var map = new LinkedMultiValueMap<String, String>();

        map.add("query", query);
        map.add("display", String.valueOf(DISPLAY));
        map.add("start", String.valueOf(START));
        map.add("sort", SORT);

        return map;
    }
}
