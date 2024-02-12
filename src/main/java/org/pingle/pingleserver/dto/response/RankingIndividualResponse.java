package org.pingle.pingleserver.dto.response;

import java.time.LocalDateTime;

public record RankingIndividualResponse (String name, LocalDateTime latestVisitedDate, Long locationCount) {
}
