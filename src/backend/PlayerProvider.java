package backend;

import models.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerProvider {

    private DatabaseConnection databaseConnection;

    private final String Q_ALLPLAYERS =
            "SELECT speler_id, naam, geslacht, gebdatum, CONCAT(a.straatnaam, ' ', a.huisnummer), a.postcode, a.woonplaats, telefoon, email, rating\n" +
            "FROM speler\n" +
            "INNER JOIN adres a on speler.adres_id = a.adres_id\n" +
            "ORDER BY speler.speler_id;";

    private final String Q_DELETEPLAYER = "DELETE FROM speler WHERE speler_id = ?";

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
     * Delete a player from the database.
     * @param id the player's id.
     * @throws SQLException
     */
    public void deletePlayer(int id) throws SQLException {
        PreparedStatement pst = databaseConnection.getConnection().prepareStatement(Q_DELETEPLAYER);
        pst.setString(1, Integer.toString(id));
        databaseConnection.executeQuery(pst);
    }

    private void getDBconnection() {
        try{
            databaseConnection = new DatabaseConnection();
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }
}
