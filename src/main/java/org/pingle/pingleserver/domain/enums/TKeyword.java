package org.pingle.pingleserver.domain.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TKeyword {
    CIRCLE("연합 동아리"),
    COLLEGE("대학교"),
    SCHOOL_CLUB("교내 동아리");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
