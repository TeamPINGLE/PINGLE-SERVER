package org.pingle.pingleserver.service;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.domain.UserMeeting;
import org.pingle.pingleserver.domain.enums.MRole;
import org.pingle.pingleserver.domain.enums.Provider;
import org.pingle.pingleserver.domain.enums.TRole;
import org.pingle.pingleserver.dto.response.UserInfoResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;
import org.pingle.pingleserver.oauth.service.AppleLoginService;
import org.pingle.pingleserver.repository.MeetingRepository;
import org.pingle.pingleserver.repository.UserMeetingRepository;
import org.pingle.pingleserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final AppleLoginService appleLoginService;
    private final MeetingRepository meetingRepository;
    private final UserMeetingRepository userMeetingRepository;

    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findByIdAndIsDeletedOrThrow(userId, false);
        return UserInfoResponse.of(user);
    }

    @Transactional
    public void leave(Long userId, String code) {
        User user = userRepository.findByIdAndIsDeletedOrThrow(userId, false);

        leaveGroups(user);
        leaveMeetings(user);
        user.softDelete();

        if (user.getProvider().equals(Provider.APPLE)){
            try {
                appleLoginService.revoke(code);
            } catch (Exception e) {
                throw new CustomException(ErrorMessage.APPLE_REVOKE_FAILED);
            }
        }
    }

    private void leaveGroups (User user){
        boolean hasAdminRole = user.getUserTeams().stream()
                .anyMatch(userTeam -> userTeam.getTeamRole().equals(TRole.OWNER));
        if (hasAdminRole) {
            throw new CustomException(ErrorMessage.GROUP_OWNER_DELETION_DENIED);
        }
        user.getUserTeams().clear();
    }

    private void leaveMeetings (User user){
        List<UserMeeting> userMeetings = userMeetingRepository.findAllByUser(user);
        userMeetings.forEach(userMeeting -> {
            if (userMeeting.getMeeting().getStartAt().isAfter(LocalDateTime.now())){
                if (userMeeting.getMeetingRole().equals(MRole.OWNER)){
                    deleteOwnerMeeting(userMeeting);
                } else {
                    userMeetingRepository.delete(userMeeting);
                }
            }
        });
    }

    private void deleteOwnerMeeting(UserMeeting userMeeting){
        Meeting meeting = userMeeting.getMeeting();
        List<UserMeeting> userMeetings = userMeetingRepository.findAllByMeeting(meeting);
        userMeetingRepository.deleteAll(userMeetings);
        meetingRepository.delete(meeting);
    }
}
