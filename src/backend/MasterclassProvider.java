package backend;

import models.Masterclass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MasterclassProvider {

    private DatabaseConnection databaseConnection;

    private final String Q_ALLMASTERCLASSES =
            "SELECT m.idMasterclass, l.stad AS Locatie,\n" +
            "       DATE_FORMAT(begintijd,  '%d-%m-%Y') AS Datum,\n" +
            "       TIME_FORMAT(begintijd, '%H:%i')     AS Begintijd,\n" +
            "       TIME_FORMAT(eindtijd, '%H:%i')      AS Eindtijd,\n" +
            "       minimumLevel                        AS 'Minimale rating',\n" +
            "       inschrijfgeld                       AS Kosten,\n" +
            "       bs.naam                             AS Begeleider\n" +
            "FROM event\n" +
            "         INNER JOIN masterclass m on event.idEvent = m.idMasterclass\n" +
            "         INNER JOIN locatie l on event.locatie = l.idLocatie\n" +
            "         INNER JOIN bekende_speler bs on m.begeleiderNr = bs.idBekend\n" +
            "ORDER BY m.idMasterclass;";

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

    private void getDBconnection() {
        try{
            databaseConnection = new DatabaseConnection();
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }
}