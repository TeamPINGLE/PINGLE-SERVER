package org.pingle.pingleserver.dto.reponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public record NaverLocationResponse(String lastBuildDate, int total, int start, int display, List<SearchLocationItem> items) {

    @Getter
    @NoArgsConstructor
    public static class SearchLocationItem{
        private String title;
        private String link;
        private String category;
        private String description;
        private String telephone;
        private String address;
        private String roadAddress;
        private int mapx;
        private int mapy;

    }
}
