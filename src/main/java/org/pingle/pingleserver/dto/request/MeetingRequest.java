package org.pingle.pingleserver.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.pingle.pingleserver.domain.enums.MCategory;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;

import java.time.LocalDateTime;

public record MeetingRequest(@NotNull MCategory category,
                             @NotNull
                             String name,
                             @NotNull
                             @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime startAt,
                             @NotNull
                             @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime endAt,
                             @NotNull
                             Double x,
                             @NotNull
                             Double y,
                             String address,
                             String roadAddress,
                             @NotNull
                             String location,
                             @Min(2)@Max(99)
                             Integer maxParticipants,
                             String chatLink) {

    @AssertTrue(message = "번개 시작 시간은 지금 시각 보다 전일 수 없습니다")
    public boolean isValidStartTime() {
        try {
            if(compareLocalDateTime(this.startAt() ,LocalDateTime.now()) < 0 )//startat 이전 false
                return false;
            return true;
        } catch (RuntimeException e) {
            throw new CustomException(ErrorMessage.BAD_REQUEST);
        }
    }
    private static int compareLocalDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.compareTo(dateTime2);
    }
}
