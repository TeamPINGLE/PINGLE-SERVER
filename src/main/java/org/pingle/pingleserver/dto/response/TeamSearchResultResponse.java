package org.pingle.pingleserver.dto.response;

import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.domain.enums.TKeyword;

public record TeamSearchResultResponse(Long id, String name, TKeyword keyword) {
    public static TeamSearchResultResponse of(Team team) {
        return new TeamSearchResultResponse(team.getId(), team.getName(), team.getKeyword());
    }
}
