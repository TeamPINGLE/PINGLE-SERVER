package org.pingle.pingleserver.fixture;

import org.pingle.pingleserver.domain.Address;
import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.Point;
import org.pingle.pingleserver.domain.Team;

public abstract class PinFixture {
    private PinFixture() {
    }

    public static Pin create(Team team) {
        return new Pin(team, new Point(1.0, 1.0), new Address("add1", "add2"), "name");
    }
}
