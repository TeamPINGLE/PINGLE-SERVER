package org.pingle.pingleserver.domain.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum MCategory {
    PLAY("play"), STUDY("study"), MULTI("multi"), OTHERS("others");

    private final String value;
}
