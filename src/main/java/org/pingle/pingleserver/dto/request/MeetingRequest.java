package org.pingle.pingleserver.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import org.pingle.pingleserver.domain.enums.MCategory;

import java.time.LocalDateTime;

public record MeetingRequest(MCategory category,
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
                             @Max(99)
                             Integer maxParticipants,
                             String chatLink) {
}
