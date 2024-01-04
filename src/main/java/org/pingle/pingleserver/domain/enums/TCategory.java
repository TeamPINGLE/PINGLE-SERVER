package org.pingle.pingleserver.domain.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TCategory {
    CIRCLE("연합동아리");

    private final String value;
}
