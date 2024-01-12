package org.pingle.pingleserver.dto.reponse;

import lombok.Builder;
import org.pingle.pingleserver.domain.enums.MCategory;

@Builder
public record MeetingResponse(Long id, MCategory category, String name, String ownerName,
                              String location, String date, String startAt, String endAt, int maxParticipants,
                              int curParticipants, boolean isParticipating, String chatLink, boolean isOwner) {
}
