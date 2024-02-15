package org.pingle.pingleserver.domain.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TKeyword {
    CIRCLE("연합 동아리"),
    SCHOOL_CLUB("교내 동아리"),
    MIDDLE_SCHOOL("중학교"),
    HIGH_SCHOOL("고등학교"),
    UNIVERSITY("대학교"),
    LECTURE("강의"),
    STUDENT_COUNCIL("학생회"),
    STUDY_GROUP("스터디"),
    CLUB("동호회"),
    PRIVATE_MEETING("사모임");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
