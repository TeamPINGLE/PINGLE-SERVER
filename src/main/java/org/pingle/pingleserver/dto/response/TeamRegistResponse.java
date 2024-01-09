package org.pingle.pingleserver.dto.response;

public record TeamRegistResponse(Long id, String name) {
    public static TeamRegistResponse of(Long id, String name) {
        return new TeamRegistResponse(id, name);
    }
}
