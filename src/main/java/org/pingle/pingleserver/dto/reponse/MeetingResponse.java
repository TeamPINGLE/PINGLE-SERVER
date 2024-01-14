package org.pingle.pingleserver.dto.reponse;

import lombok.Builder;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.enums.MCategory;

import java.time.format.DateTimeFormatter;

@Builder
public record MeetingResponse(Long id, MCategory category, String name, String ownerName,
                              String location, String date, String startAt, String endAt, int maxParticipants,
                              int curParticipants, boolean isParticipating, String chatLink, boolean isOwner) {

    public static MeetingResponse of(Meeting meeting, String ownerName, int curParticipants, boolean isParticipating, boolean isOwner) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String date = meeting.getStartAt().format(dateFormatter);
        String startAt = meeting.getStartAt().format(timeFormatter);
        String endAt = meeting.getEndAt().format(timeFormatter);

        return new MeetingResponse(meeting.getId(), meeting.getCategory(), meeting.getName(),
                ownerName, meeting.getPin().getName(), date, startAt, endAt, meeting.getMaxParticipants(),
                curParticipants, isParticipating, meeting.getChatLink(), isOwner);
    }
}
