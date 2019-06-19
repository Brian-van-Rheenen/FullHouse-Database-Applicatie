package backend;

import models.Address;
import models.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PlayerProvider {

    private DatabaseConnection databaseConnection;

    private static final String Q_ALLPLAYERS =
            "SELECT speler_id, a.adres_id, naam, geslacht, gebdatum, a.straatnaam, a.huisnummer, a.postcode, a.woonplaats, telefoon, email, rating, deleted\n" +
            "FROM speler\n" +
            "INNER JOIN adres a on speler.adres_id = a.adres_id\n" +
            "ORDER BY speler.speler_id;";

    private static final String Q_DELETEPLAYER =
            "START TRANSACTION; UPDATE speler SET adres_id = 0, naam = 'VERWIJDERD', gebdatum = '1970-01-01', geslacht = 'O', telefoon = 'VERWIJDERD', email = 'VERWIJDERD', rating = 0, deleted = TRUE WHERE speler_id = ?; DELETE FROM adres WHERE adres_id NOT IN (SELECT adres_id FROM speler) AND adres_id != 0; COMMIT;";

    private static final String Q_ADDPLAYER =
            "START TRANSACTION;\n" +
            "INSERT IGNORE INTO adres (woonplaats, straatnaam, huisnummer, postcode)\n" +
            "VALUES (?, ?, ?, ?);\n" +
            "INSERT INTO speler (adres_id, naam, gebdatum, geslacht, telefoon, email)\n" +
            "VALUES ((SELECT adres.adres_id\n" +
            "         FROM adres\n" +
            "         WHERE woonplaats = ?\n" +
            "           AND straatnaam = ?\n" +
            "           AND huisnummer = ?\n" +
            "           AND postcode = ?), ?, ?, ?, ?, ?);\n" +
            "COMMIT;";

    private static final String Q_SELECTPLAYER =
            "SELECT speler_id, a.adres_id, naam, geslacht, gebdatum, a.straatnaam, a.huisnummer, a.postcode, a.woonplaats, telefoon, email, rating, deleted\n" +
            "FROM speler\n" +
            "INNER JOIN adres a on speler.adres_id = a.adres_id\n" +
            "WHERE speler_id = ?\n" +
            "ORDER BY speler.speler_id;";

    private static final String Q_UPDATEPLAYER =
            "START TRANSACTION;\n" +
            "INSERT IGNORE INTO adres (woonplaats, straatnaam, huisnummer, postcode)\n" +
            "VALUES (?, ?, ?, ?);\n" +
            "\n" +
            "UPDATE speler\n" +
            "SET adres_id = (SELECT adres.adres_id\n" +
            "                FROM adres\n" +
            "                WHERE woonplaats = ? AND straatnaam = ? AND huisnummer = ? AND postcode = ?),\n" +
            "    naam     = ?,\n" +
            "    gebdatum = ?,\n" +
            "    geslacht = ?,\n" +
            "    email    = ?,\n" +
            "    telefoon = ?,\n" +
            "    rating   = ?,\n" +
            "    deleted  = ?\n" +
            "WHERE speler_id = ?;\n" +
            "\n" +
            "DELETE\n" +
            "FROM adres\n" +
            "WHERE adres_id NOT IN (SELECT adres_id FROM speler);\n" +
            "\n" +
            "COMMIT;";

    public PlayerProvider() {
        getDBconnection();
    }

    /**
     * Retrieve all players from the database.
     * @return an {@link ArrayList} with all {@link Player} in the database.
     * @throws SQLException when the query has failed
     */
    public ArrayList<Player> allPlayers() throws SQLException {
        ResultSet rs = databaseConnection.executeQueryAndGetData(Q_ALLPLAYERS);
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
     * @throws SQLException when the query has failed
     */
    @SuppressWarnings("Duplicates")
    public Player addPlayer(Player player) throws SQLException {
        PreparedStatement addPlayerStatement = databaseConnection
                .getConnection()
                .prepareStatement(Q_ADDPLAYER, Statement.RETURN_GENERATED_KEYS);

        Address address = player.getAddress();

        int index = 0;
        addPlayerStatement.setString(++index, address.getCity());
        addPlayerStatement.setString(++index, address.getStreet());
        addPlayerStatement.setInt(++index, address.getHouseNr());
        addPlayerStatement.setString(++index, address.getZipCode());

        addPlayerStatement.setString(++index, address.getCity());
        addPlayerStatement.setString(++index, address.getStreet());
        addPlayerStatement.setInt(++index, address.getHouseNr());
        addPlayerStatement.setString(++index, address.getZipCode());

        // Player part of query
        addPlayerStatement.setString(++index, player.getName());
        addPlayerStatement.setDate(++index, player.getDob());
        addPlayerStatement.setString(++index, player.getGender().getDatabaseRepresentation());
        addPlayerStatement.setString(++index, player.getTelephoneNR());
        addPlayerStatement.setString(++index, player.getEmail());

        addPlayerStatement.executeUpdate();

        // Update the player with the generated id
        ResultSet set = databaseConnection.getConnection().createStatement().executeQuery("SELECT LAST_INSERT_ID()");
        if(set.next()) {
            // Set the Id for the player
            player.setId(set.getInt(1));
        }

        return player;
    }

    @SuppressWarnings("Duplicates")
    public void updatePlayer(Player updated) throws SQLException {
        PreparedStatement updatePlayerStatement = databaseConnection
                .getConnection()
                .prepareStatement(Q_UPDATEPLAYER);

        Address updatedAddress = updated.getAddress();

        int index = 0;
        updatePlayerStatement.setString(++index, updatedAddress.getCity());
        updatePlayerStatement.setString(++index, updatedAddress.getStreet());
        updatePlayerStatement.setInt(++index, updatedAddress.getHouseNr());
        updatePlayerStatement.setString(++index, updatedAddress.getZipCode());

        // Address part of query
        updatePlayerStatement.setString(++index, updatedAddress.getCity());
        updatePlayerStatement.setString(++index, updatedAddress.getStreet());
        updatePlayerStatement.setInt(++index, updatedAddress.getHouseNr());
        updatePlayerStatement.setString(++index, updatedAddress.getZipCode());

        // Player part of query
        updatePlayerStatement.setString(++index, updated.getName());
        updatePlayerStatement.setDate(++index, updated.getDob());
        updatePlayerStatement.setString(++index, updated.getGender().getDatabaseRepresentation());
        updatePlayerStatement.setString(++index, updated.getEmail());
        updatePlayerStatement.setString(++index, updated.getTelephoneNR());
        updatePlayerStatement.setInt(++index, updated.getRating());
        updatePlayerStatement.setBoolean(++index, updated.isDeleted());
        // Set the Player to update
        updatePlayerStatement.setInt(++index, updated.getId());

        updatePlayerStatement.executeUpdate();
    }

    /**
     * Delete a player from the database.
     * @param id the player's id.
     * @throws SQLException when the deletion has failed
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
