package backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base class for providers that interact with the database
 */
public abstract class DatabaseProvider {

    /**
     * Query for getting the Id of the last inserted row
     */
    private final String Q_GET_LAST_ID = "SELECT LAST_INSERT_ID();";

    /**
     * Get an active database connection
     * @return an {@link Connection}
     */
    protected Connection getDatabaseConnection() {
        return DatabaseConnection.getConnection();
    }

    /**
     * Get the Id of the last inserted row
     * @return The id of the last inserted row
     * @throws SQLException Failed to connect or execute the query
     */
    protected int getLastInsertId() throws SQLException {
        Connection connection = getDatabaseConnection();

        ResultSet set = connection.createStatement().executeQuery(Q_GET_LAST_ID);
        set.next();
        return set.getInt(1);
    }

}
