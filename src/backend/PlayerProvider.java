package backend;

import models.Player;
import models.Toernooi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static backend.DB_Statements.*;
import static backend.DB_Statements.Q_ALLPLAYERS;

public class PlayerProvider {

    private DatabaseConnection databaseConnection;




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
