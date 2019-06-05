package backend;

import models.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataGetter {

    private DatabaseConnection databaseConnection;

    private final String Q_ALLPLAYERS =
            "SELECT speler_id, naam, geslacht, gebdatum, CONCAT(a.straatnaam, ' ', a.huisnummer), a.postcode, a.woonplaats, telefoon, email, rating\n" +
            "FROM speler\n" +
            "INNER JOIN adres a on speler.adres_id = a.adres_id\n" +
            "ORDER BY speler.speler_id;";

    public DataGetter() throws SQLException {
        getDBconnection();
    }

    private void getDBconnection() {
        try{
            databaseConnection = new DatabaseConnection();
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }

    public ArrayList<Player> allPlayers() throws SQLException {
        ResultSet rs = databaseConnection.sendQuery( Q_ALLPLAYERS);
        ArrayList<Player> res = new ArrayList<>();
        while (rs.next()) {
            res.add(Player.readPlayerData(rs));
        }
        return res;
    }
}
