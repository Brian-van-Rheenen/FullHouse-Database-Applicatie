package backend;

import models.Player;

import java.sql.*;
import java.util.ArrayList;
import backend.DB_Statements.*;

import static backend.DB_Statements.*;

public class PlayerProvider {

    private DatabaseConnection databaseConnection;

    private static final String Q_ALLPLAYERS =
            "SELECT speler_id, a.adres_id, naam, geslacht, gebdatum, a.straatnaam, a.huisnummer, a.postcode, a.woonplaats, telefoon, email, rating\n" +
            "FROM speler\n" +
            "INNER JOIN adres a on speler.adres_id = a.adres_id\n" +
            "ORDER BY speler.speler_id;";

    private static final String Q_DELETEPLAYER =
            "START TRANSACTION; UPDATE speler SET adres_id = 0, naam = 'VERWIJDERD', gebdatum = '1970-01-01', geslacht = 'O', telefoon = 'VERWIJDERD', email = 'VERWIJDERD', rating = 0 WHERE speler_id = ?; DELETE FROM adres WHERE adres_id NOT IN (SELECT adres_id FROM speler) AND adres_id != 0; COMMIT;";

    private static final String Q_ADDPLAYER = "START TRANSACTION;\n" +
            "INSERT INTO adres (woonplaats, straatnaam, huisnummer, postcode)\n" +
            "SELECT ?, ?, ?, ? FROM adres WHERE NOT EXISTS(SELECT * FROM adres WHERE woonplaats = ? AND straatnaam = ? AND huisnummer = ? AND postcode = ?)\n" +
            "LIMIT 1;\n" +
            "INSERT INTO speler (adres_id, naam, gebdatum, geslacht, telefoon, email)\n" +
            "VALUES (LAST_INSERT_ID(), ?, ?, ?, ?, ?);\n" +
            "COMMIT;";

    private static final String Q_SELECTPLAYER =
            "SELECT speler_id, a.adres_id, naam, geslacht, gebdatum, a.straatnaam, a.huisnummer, a.postcode, a.woonplaats, telefoon, email, rating\n" +
            "FROM speler\n" +
            "INNER JOIN adres a on speler.adres_id = a.adres_id\n" +
            "WHERE speler_id = ?\n" +
            "ORDER BY speler.speler_id;";

    private static final String Q_UPDATEPLAYER =
            "START TRANSACTION;\n" +
            "UPDATE adres\n" +
            "SET woonplaats = ?, postcode = ?, straatnaam = ?, huisnummer = ?\n" +
            "WHERE adres_id = ?;\n" +
            "UPDATE speler\n" +
            "SET naam = ?, gebdatum = ?, geslacht = ?, email = ?, telefoon = ?, rating = ?\n" +
            "WHERE speler_id = ?;\n" +
            "COMMIT;";

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

    public void updatePlayer(Player updated) throws SQLException {
        PreparedStatement updatePlayerStatement = databaseConnection
                .getConnection()
                .prepareStatement(Q_UPDATEPLAYER);

        updatePlayerStatement.setString(1, updated.getCity());
        updatePlayerStatement.setString(2, updated.getZip());
        updatePlayerStatement.setString(3, updated.getStreet());
        updatePlayerStatement.setInt(4, updated.getHouseNr());
        // Set the address to update
        updatePlayerStatement.setInt(5, updated.getAdresId());

        // Player part of query
        updatePlayerStatement.setString(6, updated.getName());
        updatePlayerStatement.setDate(7, updated.getDob());
        updatePlayerStatement.setString(8, updated.getGender());
        updatePlayerStatement.setString(9, updated.getEmail());
        updatePlayerStatement.setString(10, updated.getTelephoneNR());
        updatePlayerStatement.setInt(11, updated.getRating());
        // Set the Player to update
        updatePlayerStatement.setInt(12, updated.getId());

        updatePlayerStatement.executeUpdate();
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

    /**
     * Returns the player with the given id from the database
     * @param id The id to search for in the database
     * @return A Player object
     * @throws SQLException When connection or query fails
     */
    public Player getPlayerById(int id) throws SQLException {
        PreparedStatement playerStatement = databaseConnection
                .getConnection()
                .prepareStatement(Q_SELECTPLAYER);

        playerStatement.setInt(1, id);

        ResultSet set = playerStatement.executeQuery();
        set.next();
        return Player.readPlayerData(set);
    }

    private void getDBconnection() {
        try{
            databaseConnection = new DatabaseConnection();
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }
}
