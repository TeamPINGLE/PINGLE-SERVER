package org.pingle.pingleserver.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.enums.MCategory;
import org.pingle.pingleserver.utils.TimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Builder(access = AccessLevel.PRIVATE)
public record MyPingleResponse(Long id, MCategory category, String name, String ownerName, String location,
                               String dDay, String date, String startAt, String endAt, int maxParticipants,
                               int curParticipants, boolean isOwner) {

    private static final String DDAYPREFIX = "D";
    private static final String DDAY = "D-Day";
    private static final String DONE = "Done";

    public static MyPingleResponse of(Meeting meeting, String ownerName, boolean isOwner) {
        return MyPingleResponse.builder()
                .id(meeting.getId())
                .category(meeting.getCategory())
                .name(meeting.getName())
                .ownerName(ownerName)
                .location(meeting.getPin().getName())
                .dDay(createDDay(meeting.getStartAt(), meeting.getEndAt()))
                .date(TimeUtil.getDateFromDateTime(meeting.getStartAt()))
                .startAt(TimeUtil.getTimeFromDateTime(meeting.getStartAt()))
                .endAt(TimeUtil.getTimeFromDateTime(meeting.getEndAt()))
                .maxParticipants(meeting.getMaxParticipants())
                .curParticipants(meeting.getUserMeetingList().size())
                .isOwner(isOwner)
                .build();
    }

    private static String createDDay(LocalDateTime startAt, LocalDateTime endAt) {
        LocalDate startDate = startAt.toLocalDate();
        if(LocalDateTime.now().isAfter(endAt)) //현재가 이후면 참
            return DONE;
        if(ChronoUnit.DAYS.between(startDate, LocalDate.now()) == 0)
            return DDAY;
        return DDAYPREFIX + ChronoUnit.DAYS.between(startDate, LocalDate.now());//12일 13일 -> 1
    }
}
