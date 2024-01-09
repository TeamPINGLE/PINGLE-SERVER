package org.pingle.pingleserver.dto.response;

import org.pingle.pingleserver.domain.Team;

public record TeamDetailDto(
        Team team,
        Long meetingCount,
        Long participantCount) {
}
