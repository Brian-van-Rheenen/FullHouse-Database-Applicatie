package backend;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {

    private Connection connection;

    public DatabaseConnection() throws SQLException{
        initConnection();
    }

    /**
     * Execute a prepared statement and return data.
     * @param query a query to execute
     * @return ResultSet with the data from the query
     * @throws SQLException
     */
    public ResultSet executeQueryAndGetData(String query) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

    /**
     * Execute a prepared statement.
     * @param preparedStatement a prepared statement
     * @throws SQLException
     */
    public void executeQuery(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.execute();
    }

    public Connection getConnection() {
        return connection;
    }

    private void initConnection() throws SQLException{
        try (InputStream input = new FileInputStream("src/resources/database.properties")) {
            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            String connectionString = prop.getProperty(
                    "url",
                    // Did not find an URL, attempt to build one from the properties
                    // All properties must be lowercase
                    String.format("%s://%s/%s?%s", prop.getProperty("driver"), prop.getProperty("ip"), prop.getProperty("db"), prop.getProperty("properties"))
            );

            // DriverManager only accepts *lowercase* password & user when passing in props directly
            connection = DriverManager.getConnection(connectionString, prop);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
