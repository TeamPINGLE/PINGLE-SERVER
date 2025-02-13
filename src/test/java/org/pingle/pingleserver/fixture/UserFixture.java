package org.pingle.pingleserver.fixture;

import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.domain.enums.Provider;
import org.pingle.pingleserver.domain.enums.URole;

public abstract class UserFixture {
    private UserFixture() {
    }

    public static User create() {
        return new User("serial", "name", "email", Provider.APPLE, URole.USER, "refreshToken");
    }
}
