package org.pingle.pingleserver.dto.response;

import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.domain.enums.Provider;

public record UserInfoResponse(Long id, String name, String email, Provider provider){
    public static UserInfoResponse of(User user){
        return new UserInfoResponse(user.getId(), user.getName(), user.getEmail(), user.getProvider());
    }
}
