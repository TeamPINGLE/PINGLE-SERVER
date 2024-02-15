package org.pingle.pingleserver.dto.response;

import java.util.List;

public record SearchResponse(int searchCount, List<SearchIndividualResponse> meetings) {
    public static SearchResponse of(List<SearchIndividualResponse> meetings) {
        return new SearchResponse(meetings.size(),meetings);
    }
}
