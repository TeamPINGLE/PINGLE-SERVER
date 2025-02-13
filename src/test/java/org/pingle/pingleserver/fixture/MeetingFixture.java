package org.pingle.pingleserver.fixture;

import java.time.LocalDateTime;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.enums.MCategory;

public abstract class MeetingFixture {
    private MeetingFixture() {
    }

    public static Meeting create(Pin pin, int maxParticipants , LocalDateTime start, LocalDateTime end) {
        return new Meeting(pin, MCategory.MULTI, "name", maxParticipants, "link", start, end);
    }
}
