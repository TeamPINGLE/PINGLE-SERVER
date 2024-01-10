package org.pingle.pingleserver.dto.response;

import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.domain.UserTeam;
import org.pingle.pingleserver.domain.enums.Provider;

import java.util.List;
import java.util.stream.Collectors;

public record UserInfoResponse(Long id, String name, String email, Provider provider, List<SimpleGroupInfo> groups){
    public static UserInfoResponse of(User user){
        return new UserInfoResponse(user.getId(), user.getName(),
                user.getEmail(), user.getProvider(), getSimpleGroupsInfo(user.getUserTeams()));
    }

    private static List<SimpleGroupInfo> getSimpleGroupsInfo(List<UserTeam> userTeams){
        return userTeams.stream()
                .map(SimpleGroupInfo::of)
                .collect(Collectors.toList());
    }
}
