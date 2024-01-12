package org.pingle.pingleserver.service;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.domain.enums.Provider;
import org.pingle.pingleserver.dto.request.LeaveRequest;
import org.pingle.pingleserver.dto.response.UserInfoResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;
import org.pingle.pingleserver.oauth.service.AppleLoginService;
import org.pingle.pingleserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final AppleLoginService appleLoginService;

    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findByIdAndIsDeletedOrThrow(userId, false);
        return UserInfoResponse.of(user);
    }

    @Transactional
    public void leave(Long userId, LeaveRequest request) {
        User user = userRepository.findByIdAndIsDeletedOrThrow(userId, false);
        if (user.getProvider().equals(Provider.APPLE)){
            try {
                appleLoginService.revoke(request.code());
            } catch (Exception e) {
                throw new CustomException(ErrorMessage.APPLE_REVOKE_FAILED);
            }
        }
        user.softDelete();
    }
}
