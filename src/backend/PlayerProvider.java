package backend;

import models.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerProvider {

    private DatabaseConnection databaseConnection;

    private final String Q_ALLPLAYERS =
            "SELECT speler_id, naam, geslacht, gebdatum, a.straatnaam, a.huisnummer, a.postcode, a.woonplaats, telefoon, email, rating\n" +
            "FROM speler\n" +
            "INNER JOIN adres a on speler.adres_id = a.adres_id\n" +
            "ORDER BY speler.speler_id;";

    private final String Q_ADDPLAYER = "START TRANSACTION;\n" +
            "INSERT INTO adres (woonplaats, straatnaam, huisnummer, postcode)\n" +
            "SELECT ?, ?, ?, ? FROM adres WHERE NOT EXISTS(SELECT * FROM adres WHERE woonplaats = 'stad' AND straatnaam = 'straat' AND huisnummer = 1 AND postcode = '1234AB')\n" +
            "LIMIT 1;\n" +
            "INSERT INTO speler (adres_id, naam, gebdatum, geslacht, telefoon, email)\n" +
            "VALUES (LAST_INSERT_ID(), ?, ?, ?, ?, ?);\n" +
            "COMMIT;";

    private final String Q_DELETEPLAYER = "START TRANSACTION; UPDATE speler SET adres_id = 0, naam = 'VERWIJDERD', gebdatum = '1970-01-01', geslacht = 'O', telefoon = 'VERWIJDERD', email = 'VERWIJDERD', rating = 0 WHERE speler_id = ?; DELETE FROM adres WHERE adres_id NOT IN (SELECT adres_id FROM speler) AND adres_id != 0; COMMIT;";

    public PlayerProvider() {
        getDBconnection();
    }

    /**
     * Retrieve all players from the database.
     * @return an ArrayList with all players.
     * @throws SQLException
     */
    public ArrayList<Player> allPlayers() throws SQLException {
        ResultSet rs = databaseConnection.executeQueryAndGetData( Q_ALLPLAYERS);
        ArrayList<Player> res = new ArrayList<>();

        while (rs.next()) {
            res.add(Player.readPlayerData(rs));
        }

        return res;
    }

    /**
     * Add a new player to the database.
     * @param player the player object to add.
     * @throws SQLException
     */
    public void addPlayer(Player player) throws SQLException {
        PreparedStatement pst = databaseConnection.getConnection().prepareStatement(Q_ADDPLAYER);
        pst.setString(1, player.getCity());
        pst.setString(2, player.getStreet());
        pst.setInt(3, player.getHouseNr());
        pst.setString(4, player.getZip());
        pst.setString(5, player.getName());
        pst.setDate(6, player.getDob());
        pst.setString(7, player.getGender());
        pst.setString(8, player.getTelephoneNR());
        pst.setString(9, player.getEmail());

        databaseConnection.executeQuery(pst);
    }

    /**
     * Delete a player from the database.
     * @param id the player's id.
     * @throws SQLException
     */
    public void deletePlayer(int id) throws SQLException {
        try {
            PreparedStatement pst = databaseConnection.getConnection().prepareStatement(Q_DELETEPLAYER);
            pst.setString(1, Integer.toString(id));
            databaseConnection.executeQuery(pst);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getDBconnection() {
        try{
            databaseConnection = new DatabaseConnection();
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }
}
