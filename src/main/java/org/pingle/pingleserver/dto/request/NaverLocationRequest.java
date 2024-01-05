package org.pingle.pingleserver.dto.request;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class NaverLocationRequest {

    private String query = "";

    private static final int DISPLAY = 5;

    private static final int START = 1;

    private static final String SORT = "random";

    public NaverLocationRequest(String query) {
        this.query = query;
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
