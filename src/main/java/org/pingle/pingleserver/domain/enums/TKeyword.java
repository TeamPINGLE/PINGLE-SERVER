package org.pingle.pingleserver.domain.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TKeyword {
    CIRCLE("연합동아리"),
    SCHOOL_CLUB("교내동아리"),
    STUDENT_COUNCIL("학생회"),
    UNIVERSITY("대학교"),
    HIGH_SCHOOL("고등학교"),
    MIDDLE_SCHOOL("중학교"),
    LECTURE("강의"),
    STUDY_GROUP("스터디"),
    PRIVATE_MEETING("사모임"),
    CLUB("동호회"),
    ETC("기타");

    private final String value;
}
