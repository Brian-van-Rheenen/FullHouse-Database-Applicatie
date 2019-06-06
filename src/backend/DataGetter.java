package backend;

import models.Player;
import models.Toernooi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataGetter {

    private DatabaseConnection databaseConnection;


    private final String Q_ALLPLAYERS = "SELECT naam AS Naam," +
            "rating as Rating, \n" +
            "a.straatnaam as Straatnaam ," +
            " a.huisnummer AS Adres,\n" +
            "a.postcode  AS Postcode,\n" +
            "       a.woonplaats                            as Woonplaats,\n" +
            "      gebdatum AS geboortedatum," +
            "       email                                   AS Email,\n" +
            "      telefoon                                AS Telefoon,\n" +
            "       geslacht                                AS Geslacht\n" +
            "FROM speler\n" +
            "         INNER JOIN adres a on speler.adres_id = a.adres_id\n" +
            "ORDER BY speler.speler_id;";

    private final String Q_TOURNAMENTS="select toernooiCode, thema, begintijd from toernooi join event on toernooi.idToernooi = event.idEvent;";

    public DataGetter() throws SQLException {
        getDBconnection();
    }

    private void getDBconnection() {
        try {
            databaseConnection = new DatabaseConnection();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    public ArrayList <Toernooi> getAllTournaments() throws SQLException{
        ResultSet rs= databaseConnection.sendQuery(Q_TOURNAMENTS);
        ArrayList<Toernooi> res = new ArrayList<>();
        while (rs.next()) {
            res.add(null);
        }
        return res;
    }

    public ArrayList<Player> allPlayers() throws SQLException {
        ResultSet rs = databaseConnection.sendQuery(Q_ALLPLAYERS);
        ArrayList<Player> res = new ArrayList<>();
        while (rs.next()) {
            res.add(Player.readPlayerData(rs));
        }
        return res;
    }
}
