package backend;

import components.dialogs.exceptions.ExceptionDialog;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {

    private static Connection connection;

    /**
     * Check if a database connection has already been made. If not, create that connection.
     * @return the database connection object.
     */
    public static Connection getConnection() {
        if (connection != null) return connection;

        connection = initConnection();
        return connection;
    }

    /**
     * Initialize a connection to the database.
     * @return the database connection object.
     */
    private static Connection initConnection() {
        try (InputStream input = new FileInputStream("src/resources/database.properties")) {
            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            String connectionString = prop.getProperty(
                    "url",
                    // Did not find an URL, attempt to build one from the properties
                    // All properties must be lowercase
                    String.format("%s://%s:%s/%s?%s", prop.getProperty("driver"), prop.getProperty("ip"), prop.getProperty("port"), prop.getProperty("db"), prop.getProperty("properties"))
            );

            try {
                // DriverManager only accepts *lowercase* password & user when passing in props directly
                connection = DriverManager.getConnection(connectionString, prop);
            }
            catch (SQLException sql) {
                // If the connection fails, show an error message and stop the program.
                new ExceptionDialog("De database gegevens zijn niet correct of de server van de database is offline.\nProbeer het opnieuw.");
                System.exit(1);
            }
        } catch (IOException ex) {
            // If the system cannot find the database properties file, show an error message and stop the program.
            new ExceptionDialog("Het systeem kan het bestand met de database gegevens niet vinden.\n\nControleer of het pad en de bestandsnaam juist zijn opgegeven.");
            System.exit(1);
        }

        return connection;
    }

    /**
     * Execute a prepared statement and return data.
     * @param query a query to execute
     * @return ResultSet with the data from the query
     * @throws SQLException failed to connect or execute the query
     */
    public static ResultSet executeQueryAndGetData(String query) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement(query);
        return preparedStatement.executeQuery();
    }

    /**
     * Execute a prepared statement.
     * @param preparedStatement a prepared statement
     * @throws SQLException
     */
    public static void executeQuery(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.execute();
    }
}
