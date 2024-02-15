package org.pingle.pingleserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pingle.pingleserver.domain.enums.TKeyword;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String link;

    private String code;

    private String email;

    @Enumerated(EnumType.STRING)
    private TKeyword keyword;

    @Builder
    public Team(String name, String email, String code, TKeyword keyword) {
        this.name = name;
        this.email = email;
        this.code = code;
        this.keyword = keyword;
    }
}
