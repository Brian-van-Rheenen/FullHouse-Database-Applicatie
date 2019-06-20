package backend;

import models.Tournament;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TournamentProvider {

    private DatabaseConnection databaseConnection;

    private final String Q_ALLTOURNAMENTS =
            "SELECT t.idToernooi                                     AS ID,\n" +
            "       l.stad                                           AS Locatie,\n" +
            "        e.capaciteit AS Capaciteit,\n" +
            "       DATE_FORMAT(begintijd, '%d-%m-%Y')               AS Begindatum,\n" +
            "       TIME_FORMAT(begintijd, '%H:%i')                  AS Begintijd,\n" +
            "       DATE_FORMAT(eindtijd, '%d-%m-%Y')                AS Einddatum,\n" +
            "       TIME_FORMAT(eindtijd, '%H:%i')                   AS Eindtijd,\n" +
            "       e.inschrijfgeld                                  AS Kosten,\n" +
            "       t.thema                                          AS Thema,\n" +
            "       DATE_FORMAT(uiterste_inschrijfdatum, '%d-%m-%Y') AS 'Uiterste inschrijfdatum',\n" +
            "       t.toegang_beperking                              AS Toegangsbeperking\n" +
            "FROM event e\n" +
            "         INNER JOIN locatie l on e.locatie = l.idLocatie\n" +
            "         INNER JOIN toernooi t ON idEvent = t.idToernooi\n" +
            "ORDER BY t.idToernooi;";

    public TournamentProvider() {
        getDBconnection();
    }

    public ArrayList<Tournament> getTournaments() throws SQLException {
        ArrayList<Tournament> res = new ArrayList<>();

        ResultSet rs = databaseConnection.executeQueryAndGetData(Q_ALLTOURNAMENTS);
        while(rs.next()){
            res.add(Tournament.readTournament(rs));
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
