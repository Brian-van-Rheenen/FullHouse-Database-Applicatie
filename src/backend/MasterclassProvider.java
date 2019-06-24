package backend;

import models.Masterclass;
import models.Participant;

import java.sql.*;
import java.util.ArrayList;

public class MasterclassProvider extends DatabaseProvider {

    //region Queries



    private static final String Q_ALLMASTERCLASSES =
            "SELECT m.idMasterclass AS ID,\n" +
            "l.stad AS Locatie,\n" +
            "       event.capaciteit as Capaciteit,\n" +
            "       DATE_FORMAT(begintijd,  '%d-%m-%Y') AS BeginDatum,\n" +
            "       TIME_FORMAT(begintijd, '%H:%i')     AS Begintijd,\n" +
            "       DATE_FORMAT(eindtijd,  '%d-%m-%Y')  AS EindDatum,\n" +
            "       TIME_FORMAT(eindtijd, '%H:%i')      AS Eindtijd,\n" +
            "       minimumLevel                        AS 'Minimale rating',\n" +
            "       inschrijfgeld                       AS Kosten,\n" +
            "       bs.naam                             AS Begeleider,\n" +
            "       bs.idBekend                         AS BegeleiderId\n" +
            "FROM event\n" +
            "         INNER JOIN masterclass m on event.idEvent = m.idMasterclass\n" +
            "         INNER JOIN locatie l on event.locatie = l.idLocatie\n" +
            "         INNER JOIN bekende_speler bs on m.begeleiderNr = bs.idBekend\n" +
            "ORDER BY m.idMasterclass;";

    private static final String Q_ADDMASTERCLASS = "START TRANSACTION;\n" +
            "INSERT INTO event (locatie, capaciteit, begintijd, eindtijd, inschrijfgeld)\n" +
            "VALUES ((SELECT idLocatie FROM locatie WHERE stad = ? LIMIT 1), ?, ?, ?, ?);\n" +
            "INSERT INTO masterclass (idMasterclass, minimumLevel, begeleiderNr)\n" +
            "VALUES (LAST_INSERT_ID(), ?, (SELECT idBekend FROM bekende_speler WHERE naam = ?));\n" +
            "COMMIT;";

    private static final String Q_FILTERPLAYERSBYRATING = "SELECT m.idMasterclass AS Masterclasscode,\n" +
            "       s.naam          AS Naam,\n" +
            "       s.rating        AS Rating\n" +
            "FROM masterclass m\n" +
            "         JOIN masterclass_deelname md on m.idMasterclass = md.mc_code\n" +
            "         JOIN speler s on md.gast = s.speler_id\n" +
            "WHERE rating >= ? AND s.deleted = 0\n" +
            "ORDER BY s.rating;";

    private static final String Q_UPDATEMASTERCLASS =
            "START TRANSACTION;\n" +
            "UPDATE event\n" +
            "SET locatie = (SELECT idLocatie FROM locatie WHERE stad = ?), capaciteit = ?, begintijd = ?, eindtijd = ?, inschrijfgeld = ?\n" +
            "WHERE idEvent = ?;\n" +
            "UPDATE masterclass\n" +
            "SET minimumLevel = ?, begeleiderNr = (SELECT idBekend FROM bekende_speler WHERE naam = ?)\n" +
            "WHERE idMasterclass = ?;\n" +
            "COMMIT;";

    private static final String Q_ALLLOCATIONS =
            "SELECT stad FROM locatie;";

    private static final String Q_ALLFAMOUSPLAYERS =
            "SELECT naam FROM bekende_speler;";

    //endregion Queries

    /**
     * Retrieve all {@link Masterclass} from the database.
     * @return an ArrayList with all players.
     * @throws SQLException when the query fails
     */
    public ArrayList<Masterclass> allMasterclasses() throws SQLException {
        ResultSet rs = DatabaseConnection.executeQueryAndGetData(Q_ALLMASTERCLASSES);
        ArrayList<Masterclass> res = new ArrayList<>();

        while (rs.next()) {
            res.add(Masterclass.readMasterclassData(rs));
        }

        return res;
    }

    /**
     * Add a new {@link Masterclass} to the database.
     * @param masterclass the masterclass object to add.
     * @return Masterclass object with an updated id
     * @throws SQLException when the query fails
     */
    @SuppressWarnings("Duplicates")
    public Masterclass addMasterclass(Masterclass masterclass) throws SQLException {
        PreparedStatement addMasterclassStatement = getDatabaseConnection()
                .prepareStatement(Q_ADDMASTERCLASS, Statement.RETURN_GENERATED_KEYS);

        addMasterclassStatement.setString(1, masterclass.getCity());
        addMasterclassStatement.setInt(2, masterclass.getCapacity());
        addMasterclassStatement.setString(3, masterclass.createDateTime(masterclass.getStartDate(), masterclass.getStartTime()));
        addMasterclassStatement.setString(4, masterclass.createDateTime(masterclass.getEndDate(), masterclass.getEndTime()));
        addMasterclassStatement.setInt(5, masterclass.getEntranceFee());
        addMasterclassStatement.setInt(6, masterclass.getMinimumRating());
        addMasterclassStatement.setString(7, masterclass.getMentor());

        addMasterclassStatement.executeUpdate();

        // Update the masterclass with the last insert id
        masterclass.setId(getLastInsertId());

        return masterclass;
    }


    @SuppressWarnings("Duplicates")
    public void updateMasterclass(Masterclass updated) throws SQLException {
        PreparedStatement updateMasterclassStatement = getDatabaseConnection()
                .prepareStatement(Q_UPDATEMASTERCLASS);

        updateMasterclassStatement.setString(1, updated.getCity());
        updateMasterclassStatement.setInt(2, updated.getCapacity());
        updateMasterclassStatement.setString(3, updated.createDateTime(updated.getStartDate(), updated.getStartTime()));
        updateMasterclassStatement.setString(4, updated.createDateTime(updated.getEndDate(), updated.getEndTime()));
        updateMasterclassStatement.setInt(5, updated.getEntranceFee());
        // Set the Event to update
        updateMasterclassStatement.setInt(6, updated.getId());

        updateMasterclassStatement.setInt(7, updated.getMinimumRating());
        updateMasterclassStatement.setString(8, updated.getMentor());
        // Set the Masterclass to update
        updateMasterclassStatement.setInt(9, updated.getId());

        updateMasterclassStatement.executeUpdate();
    }

    public ResultSet filterPlayersByRating(int rating) throws SQLException {
        PreparedStatement pst = getDatabaseConnection().prepareStatement(Q_FILTERPLAYERSBYRATING);
        pst.setInt(1, rating);

        return pst.executeQuery();
    }

    @SuppressWarnings("Duplicates")
    public ArrayList<String> getAllLocations() throws SQLException {
        ResultSet rs = DatabaseConnection.executeQueryAndGetData(Q_ALLLOCATIONS);
        ArrayList<String> res = new ArrayList<>();

        while (rs.next()) {
            res.add(rs.getString(1));
        }

        return res;
    }

    @SuppressWarnings("Duplicates")
    public ArrayList<String> getAllFamousPlayers() throws SQLException {
        ResultSet rs = DatabaseConnection.executeQueryAndGetData(Q_ALLFAMOUSPLAYERS);
        ArrayList<String> res = new ArrayList<>();

        while (rs.next()) {
            res.add(rs.getString(1));
        }

        return res;
    }
}