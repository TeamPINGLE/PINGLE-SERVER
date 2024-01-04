package org.pingle.pingleserver.domain.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum MRole {
    MOWNER("owner"), MPARTICIPANTS("participants");

    private final String value;
}
