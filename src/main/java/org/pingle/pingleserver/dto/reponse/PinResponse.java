package org.pingle.pingleserver.dto.reponse;

import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.enums.MCategory;

import java.util.Comparator;
import java.util.List;

public record PinResponse(Long id, Double x, Double y, MCategory category, int meetingCount) {
    public static PinResponse of(Pin pin) {
        return new PinResponse(pin.getId(),pin.getPoint().getX(), pin.getPoint().getY(),
                getMostRecentMeetingCategoryOfPin(pin), getMeetingCount(pin));

    }
    private static MCategory getMostRecentMeetingCategoryOfPin (Pin pin) {
        Comparator<Meeting> comparator = Comparator.comparing(Meeting::getStartAt);
        List<Meeting> meetingList = pin.getMeetingList();
        meetingList.sort(comparator);
        return meetingList.get(0).getCategory();
    }

    private static int getMeetingCount(Pin pin) {
        return pin.getMeetingList().size();
    }

}
