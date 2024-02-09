package org.pingle.pingleserver.dto.reponse;

import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.domain.enums.TKeyword;

public record TeamCreationResponse (
        Long id,
        String name,
        String email,
        String code,
        TKeyword keyword
) {
    public static TeamCreationResponse of(Team team) {
        return new TeamCreationResponse(team.getId(), team.getName(), team.getEmail(), team.getCode(), team.getKeyword());
    }
}
