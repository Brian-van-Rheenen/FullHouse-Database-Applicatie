package backend;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DatabaseConnection {

    private Connection connection;

    public DatabaseConnection() throws SQLException{

      initConnection();
    }

    public ResultSet sendQuery(String query) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
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
                    String.format("%s://%s/%s", prop.getProperty("driver"), prop.getProperty("ip"), prop.getProperty("db"))
            );

            System.out.println(connectionString);

            // DriverManager only accepts *lowercase* password & user when passing in props directly
            connection = DriverManager.getConnection(connectionString, prop);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
