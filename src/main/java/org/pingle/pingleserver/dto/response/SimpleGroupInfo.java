package org.pingle.pingleserver.dto.response;

import org.pingle.pingleserver.domain.UserTeam;

public record SimpleGroupInfo(Long id, String name) {
    public static SimpleGroupInfo of(UserTeam userTeam) {
        return new SimpleGroupInfo(userTeam.getTeam().getId(), userTeam.getTeam().getName());
    }
}
