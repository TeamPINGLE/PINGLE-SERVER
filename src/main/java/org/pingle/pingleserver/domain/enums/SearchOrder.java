package org.pingle.pingleserver.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchOrder {
    NEW("new"), UPCOMING("upcoming");
    private final String value;
}
