package org.pingle.pingleserver.fixture;

import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.domain.enums.TKeyword;

public abstract class TeamFixture {
    private TeamFixture() {
    }

    public static Team create() {
        return new Team("team1", "team1", "code", TKeyword.ETC);
    }
}
