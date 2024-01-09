package org.pingle.pingleserver.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MCategory {
    PLAY("play"), STUDY("study"), MULTI("multi"), OTHERS("others");

    private final String value;
}
