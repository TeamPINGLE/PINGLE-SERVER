package org.pingle.pingleserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pingle.pingleserver.domain.enums.TKeyword;

import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private TKeyword keyword;

    @OneToMany(mappedBy = "team")
    private List<UserTeam> userTeams;

}
