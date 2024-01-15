package org.pingle.pingleserver.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.enums.MCategory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Builder(access = AccessLevel.PRIVATE)
public record MyPingleResponse(Long id, MCategory category, String name, String ownerName, String location,
                               String dDay, String date, String startAt, String endAt, int maxParticipants,
                               int curParticipants, boolean isOwner) {

    private static final String DDAYPREFIX = "D";
    private static final String DDAY = "D-Day";
    private static final String DONE = "Done";

    public static MyPingleResponse of(Meeting meeting, String ownerName, boolean isOwner) {//유저아이디랑 번개 아이디가 필요.
        return MyPingleResponse.builder()
                .id(meeting.getId())
                .category(meeting.getCategory())
                .name(meeting.getName())
                .ownerName(ownerName)
                .location(meeting.getPin().getName())
                .dDay(createDDay(meeting.getStartAt()))
                .date(getDateFromDateTime(meeting.getStartAt()))
                .startAt(getTimeFromDateTime(meeting.getStartAt()))
                .endAt(getTimeFromDateTime(meeting.getEndAt()))
                .maxParticipants(meeting.getMaxParticipants())
                .curParticipants(meeting.getUserMeetingList().size())
                .isOwner(isOwner)
                .build();
    }

    private static String createDDay(LocalDateTime startAt) {
        LocalDate startDate = startAt.toLocalDate();
        if(ChronoUnit.DAYS.between(startDate, LocalDate.now()) > 0)
            return DONE;
        if(ChronoUnit.DAYS.between(startDate, LocalDate.now()) == 0)
            return DDAY;
        return DDAYPREFIX + ChronoUnit.DAYS.between(startDate, LocalDate.now());//12일 13일 -> 1
    }
    private static String getDateFromDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(formatter);
    }
    private static String getTimeFromDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return localDateTime.format(formatter);
    }
}
