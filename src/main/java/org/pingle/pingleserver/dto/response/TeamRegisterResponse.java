package org.pingle.pingleserver.dto.response;

public record TeamRegisterResponse(Long id, String name) {
    public static TeamRegisterResponse of(Long id, String name) {
        return new TeamRegisterResponse(id, name);
    }
}
