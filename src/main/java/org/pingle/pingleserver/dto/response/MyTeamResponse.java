package org.pingle.pingleserver.dto.response;

import org.pingle.pingleserver.domain.Team;

public record MyTeamResponse (
        Long id, String keyword, String name, Long meetingCount, Long participantCount, boolean isOwner, String code) {
    public static MyTeamResponse of(Team team, Long meetingCount, Long participantCount, boolean isOwner) {
        return new MyTeamResponse(
                team.getId(),
                team.getKeyword().toString(),
                team.getName(),
                meetingCount,
                participantCount,
                isOwner,
                team.getCode());
    }
}
