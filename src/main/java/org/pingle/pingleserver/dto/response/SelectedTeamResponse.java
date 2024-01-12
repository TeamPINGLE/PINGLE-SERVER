package org.pingle.pingleserver.dto.response;

import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.domain.enums.TKeyword;

public record SelectedTeamResponse(
        Long id, String keyword, String name, Long meetingCount, Long participantCount) {

    public static SelectedTeamResponse of(Team team, Long meetingCount, Long participantCount) {
        return new SelectedTeamResponse(team.getId(), team.getKeyword().toString(), team.getName(), meetingCount, participantCount);
    }

}
