package org.pingle.pingleserver.dto.response;

import java.util.List;

public record RankingResponse (int meetingCount, List<RankingIndividualResponse> locations)  {
    public static RankingResponse of (List<RankingIndividualResponse> responses) {
        return new RankingResponse(getSumOfMeetings(responses), responses);
    }

    private static int getSumOfMeetings (List<RankingIndividualResponse> responses) {
        int sum = 0;
        for (RankingIndividualResponse response : responses) {
            sum += response.locationCount();
        }
        return sum;
    }
}
