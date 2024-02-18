package org.pingle.pingleserver.dto.response;

import org.pingle.pingleserver.domain.Team;

public record TeamSearchResultResponse(Long id, String name, String keyword) {
    public static TeamSearchResultResponse of(Team team) {
        return new TeamSearchResultResponse(team.getId(), team.getName(), team.getKeyword().getValue());
    }
}
