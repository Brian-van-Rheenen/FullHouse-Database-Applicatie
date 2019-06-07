package backend;

import models.Player;

import java.sql.*;
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
            "SELECT ?, ?, ?, ? FROM adres WHERE NOT EXISTS(SELECT * FROM adres WHERE woonplaats = ? AND straatnaam = ? AND huisnummer = ? AND postcode = ?)\n" +
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
     * @return Player object with an updated id
     * @throws SQLException
     */
    public Player addPlayer(Player player) throws SQLException {
        PreparedStatement addPlayerStatement = databaseConnection
                .getConnection()
                .prepareStatement(Q_ADDPLAYER, Statement.RETURN_GENERATED_KEYS);

        addPlayerStatement.setString(1, player.getCity());
        addPlayerStatement.setString(2, player.getStreet());
        addPlayerStatement.setInt(3, player.getHouseNr());
        addPlayerStatement.setString(4, player.getZip());
        addPlayerStatement.setString(5, player.getCity());
        addPlayerStatement.setString(6, player.getStreet());
        addPlayerStatement.setInt(7, player.getHouseNr());
        addPlayerStatement.setString(8, player.getZip());

        // Player part of query
        addPlayerStatement.setString(9, player.getName());
        addPlayerStatement.setDate(10, player.getDob());
        addPlayerStatement.setString(11, player.getGender());
        addPlayerStatement.setString(12, player.getTelephoneNR());
        addPlayerStatement.setString(13, player.getEmail());

        addPlayerStatement.executeUpdate();

        // Update the player with the generated id
        ResultSet set = databaseConnection.getConnection().createStatement().executeQuery("SELECT LAST_INSERT_ID()");
        if(set.next()) {
            // Set the Id for the player
            player.setId(set.getInt(1));
        }

        return player;
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
