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



    public Connection getConnection() {
        return connection;
    }

    private void initConnection() throws SQLException{
        try (InputStream input = new FileInputStream("src/resources/database.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            System.out.println(prop.getProperty("url"));
            System.out.println(prop.getProperty("user"));
            System.out.println(prop.getProperty("password"));

           connection =   DriverManager.getConnection(prop.getProperty("url"), prop);


        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
