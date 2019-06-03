package backend;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DataHandler {

    private Connection connection;

    public DataHandler() {

      initConnection();

        try {

            submitQuery(" select naam from `18083250`.speler;").forEach(System.out::println);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> submitQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ArrayList<String> res = new ArrayList<>();

        while (resultSet.next()) {
            int k = 1;
            res.add(resultSet.getString(k));
            k++;
        }
        return res;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initConnection(){
        try (InputStream input = new FileInputStream("C:\\Users\\Davidius\\IdeaProjects\\FullHouse-Database-Applicatie\\src\\resources\\database.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            System.out.println(prop.getProperty("url"));
            System.out.println(prop.getProperty("user"));
            System.out.println(prop.getProperty("password"));

           connection =   DriverManager.getConnection(prop.getProperty("url"), prop);


        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed");
        }

    }
}
