package org.pingle.pingleserver.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
    KAKAO("KAKAO"),
    APPLE("APPLE");

    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
