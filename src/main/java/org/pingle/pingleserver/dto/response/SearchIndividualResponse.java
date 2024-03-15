package org.pingle.pingleserver.dto.response;

import lombok.Builder;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.UserMeeting;
import org.pingle.pingleserver.domain.enums.MCategory;
import org.pingle.pingleserver.domain.enums.MRole;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;
import org.pingle.pingleserver.utils.TimeUtil;

import java.util.List;

@Builder
public record SearchIndividualResponse(Long id,MCategory category, String name, String ownerName,
                              String location, String date, String startAt, String endAt, int maxParticipants,
                              int curParticipants, boolean isParticipating, String chatLink, boolean isOwner) {

    public static SearchIndividualResponse of (Meeting meeting, Long userId) {
        return new SearchIndividualResponse(meeting.getId(), meeting.getCategory(), meeting.getName(), getOwnerName(meeting),
                meeting.getPin().getName(), TimeUtil.getDateFromDateTime(meeting.getStartAt()),
                TimeUtil.getTimeFromDateTime(meeting.getStartAt()), TimeUtil.getTimeFromDateTime(meeting.getEndAt()),
                meeting.getMaxParticipants(), meeting.getUserMeetingList().size(),
                isParticipating(meeting, userId), meeting.getChatLink(), isOwner(meeting, userId));
    }

    private static String getOwnerName(Meeting meeting) {
        List<UserMeeting> userMeetings = meeting.getUserMeetingList();
        if(userMeetings.isEmpty()) throw new CustomException(ErrorMessage.RESOURCE_NOT_FOUND);
        for (UserMeeting userMeeting : userMeetings) {
            if(userMeeting.getMeetingRole().equals(MRole.OWNER)) return userMeeting.getUser().getValidName();
        }
        return null;
    }

    private static boolean isParticipating(Meeting meeting, Long userId) {
        List<UserMeeting> userMeetings = meeting.getUserMeetingList();
        for (UserMeeting userMeeting : userMeetings) {
            if(userMeeting.getUser().getId().equals(userId)) return true;
        }
        return false;
    }
    private static boolean isOwner(Meeting meeting, Long userId) {
        List<UserMeeting> userMeetings = meeting.getUserMeetingList();
        for (UserMeeting userMeeting : userMeetings) {
            if (userMeeting.getMeetingRole().equals(MRole.OWNER) && userMeeting.getUser().getId().equals(userId)) return true;
        }
        return false;
    }

}

