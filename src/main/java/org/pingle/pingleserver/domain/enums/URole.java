package org.pingle.pingleserver.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum URole {
    USER("USER"),
    ADMIN("ADMIN");

    private final String name;
}
