package org.pingle.pingleserver.dto.response;

import org.pingle.pingleserver.domain.UserMeeting;

import java.util.Collections;
import java.util.List;

public record ParticipantsResponse(
        List<String> participants
) {
    public static ParticipantsResponse of(List<UserMeeting> userMeetings) {
        Collections.sort(userMeetings);
        List<String> participantNames = userMeetings.stream().map(userMeeting ->
                userMeeting.getUser().getValidName()).toList();
        return new ParticipantsResponse(participantNames);
    }
}
