package backend;

import models.Masterclass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MasterclassProvider {

    private DatabaseConnection databaseConnection;

    private final String Q_ALLMASTERCLASSES =
            "SELECT m.idMasterclass, l.stad AS Locatie,\n" +
            "       event.capaciteit as Capaciteit,\n"+
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

    private final String Q_ADDMASTERCLASS = "START TRANSACTION;\n" +
            "INSERT INTO event (locatie, capaciteit, begintijd, eindtijd, inschrijfgeld)\n" +
            "VALUES ((SELECT idLocatie FROM locatie WHERE stad = ? LIMIT 1), ?, ?, ?, ?);\n" +
            "INSERT INTO masterclass (idMasterclass, minimumLevel, begeleiderNr)\n" +
            "VALUES (LAST_INSERT_ID(), ?, ?);\n" +
            "COMMIT;";

    public MasterclassProvider() {
        getDBconnection();
    }

    /**
     * Retrieve all players from the database.
     * @return an ArrayList with all players.
     * @throws SQLException
     */
    public ArrayList<Masterclass> allMasterclasses() throws SQLException {
        ResultSet rs = databaseConnection.executeQueryAndGetData(Q_ALLMASTERCLASSES);
        ArrayList<Masterclass> res = new ArrayList<>();

        while (rs.next()) {
            res.add(Masterclass.readMasterclassData(rs));
        }

        return res;
    }

    /**
     * Add a new masterclass to the database.
     * @param masterclass the masterclass object to add.
     * @return Masterclass object with an updated id
     * @throws SQLException
     */
    @SuppressWarnings("Duplicates")
    public Masterclass addMasterclass(Masterclass masterclass) throws SQLException {
        PreparedStatement addMasterclassStatement = databaseConnection
                .getConnection()
                .prepareStatement(Q_ADDMASTERCLASS, Statement.RETURN_GENERATED_KEYS);

        addMasterclassStatement.setString(1, masterclass.getCity());
        addMasterclassStatement.setInt(2, masterclass.getCapacity());
        addMasterclassStatement.setString(3, masterclass.createDateTime(masterclass.getBeginDate(), masterclass.getBeginTime()));
        addMasterclassStatement.setString(4, masterclass.createDateTime(masterclass.getEndDate(), masterclass.getEndTime()));
        addMasterclassStatement.setInt(5, masterclass.getPrice());
        addMasterclassStatement.setInt(6, masterclass.getMinimumRating());
        addMasterclassStatement.setInt(7, masterclass.getMentorId());

        addMasterclassStatement.executeUpdate();

        // Update the masterclass with the generated id
        ResultSet set = databaseConnection.getConnection().createStatement().executeQuery("SELECT LAST_INSERT_ID(), naam FROM bekende_speler WHERE idBekend = " + masterclass.getMentorId());
        if(set.next()) {
            // Set the Id for the masterclass
            masterclass.setId(set.getInt(1));
            masterclass.setMentor(set.getString(2));
        }

        return masterclass;
    }

    private void getDBconnection() {
        try{
            databaseConnection = new DatabaseConnection();
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }
}