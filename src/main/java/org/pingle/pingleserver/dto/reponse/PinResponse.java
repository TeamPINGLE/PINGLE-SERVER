package org.pingle.pingleserver.dto.reponse;

import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.enums.MCategory;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public record PinResponse(Long id, Double x, Double y, MCategory category, int meetingCount) {
    public static PinResponse ofWithNoFilter(Pin pin) {
        return new PinResponse(pin.getId(),pin.getPoint().getX(), pin.getPoint().getY(),
                getMostRecentMeetingCategoryOfPin(pin), getMeetingCount(pin));

    }
    public static PinResponse ofWithFilter(Pin pin, MCategory mCategory) {
        return new PinResponse(pin.getId(),pin.getPoint().getX(), pin.getPoint().getY(),
                mCategory, getMeetingCountWithFilter(pin, mCategory));

    }
    private static MCategory getMostRecentMeetingCategoryOfPin (Pin pin) {
        List<Meeting> meetingList = pin.getMeetingList();
        LocalDateTime currentDateTime = LocalDateTime.now();
        meetingList.removeIf(meeting -> meeting.getStartAt() != null && meeting.getStartAt().isBefore(currentDateTime));
        meetingList.sort(Comparator.comparing(Meeting::getStartAt));

        return meetingList.get(0).getCategory();
    }

    private static int getMeetingCount(Pin pin) {
        List<Meeting> meetingList = pin.getMeetingList();
        LocalDateTime currentDateTime = LocalDateTime.now();
        Iterator<Meeting> iterator = meetingList.iterator();
        while (iterator.hasNext()) {
            Meeting meeting = iterator.next();
            if (meeting.getStartAt() != null && meeting.getStartAt().isBefore(currentDateTime)) {
                iterator.remove();
            }
        }
        return meetingList.size();
    }
    private static int getMeetingCountWithFilter(Pin pin, MCategory category) {
        int count = 0;
        List<Meeting> meetings = pin.getMeetingList();
        LocalDateTime currentDateTime = LocalDateTime.now();
        Iterator<Meeting> iterator = meetings.iterator();
        while (iterator.hasNext()) {
            Meeting meeting = iterator.next();
            if (meeting.getStartAt() != null && meeting.getStartAt().isBefore(currentDateTime)) {//핀의 미팅 중에서 유효시간반빼와
                iterator.remove();
            }
        }
        for(Meeting meeting : meetings) {
            if(meeting.getCategory().getValue().equals(category.getValue()))
                count++;
        }

        return count;
    }

}
