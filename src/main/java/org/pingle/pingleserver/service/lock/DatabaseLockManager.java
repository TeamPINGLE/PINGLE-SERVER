package org.pingle.pingleserver.service.lock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;
import org.pingle.pingleserver.service.LockManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLockManager implements LockManager {

    private final DataSource lockDataSource;

    public DatabaseLockManager(@Qualifier("lockDataSource") DataSource lockDataSource) {
        this.lockDataSource = lockDataSource;
    }

    public void executeWithLock(String key, Runnable runnable) {
        try (Connection connection = lockDataSource.getConnection()) {
            try {
                getLock(connection, key);
                runnable.run();
            } finally {
                releaseLock(connection, key);
            }
        } catch (SQLException e) {
            throw new CustomException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    private void getLock(final Connection connection, final String key) {
        String sql = "SELECT get_lock(?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, key);
            stmt.setInt(2, 3000);
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new CustomException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    private void releaseLock(final Connection connection, final String key) {
        String sql = "SELECT RELEASE_LOCK(?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, key);
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new CustomException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
