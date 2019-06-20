package backend;

import models.Deelname;
import models.Player;
import models.Tournament;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class TournamentProvider {

    private DatabaseConnection databaseConnection;
    private ArrayList<Tournament> tournaments=new ArrayList<>();

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

    private static final String Q_PARTCIPANTS =
            "select speler_id, a.adres_id, naam, geslacht, gebdatum, a.straatnaam, a.huisnummer," +
                    " a.postcode,  a.woonplaats, telefoon, email, rating, deleted, toernooiCode, betaald " +
                    "FROM toernooi_deelname " +
                    "INNER JOIN speler on toernooi_deelname.speler=speler_id " +
                    "INNER JOIN toernooi on toernooiCode=idToernooi "   +
                    "INNER JOIN adres a on speler.adres_id = a.adres_id;";





    public TournamentProvider() {
        try {
            databaseConnection = new DatabaseConnection();
            initTournamentList();
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    public ArrayList<Tournament> getTournaments()  {
       return tournaments;
    }

    private ResultSet queryParticipants() throws SQLException{
        return  databaseConnection.getConnection().prepareStatement(Q_PARTCIPANTS).executeQuery();

    }

    private void addParticipants() throws SQLException{


        ResultSet   rs = queryParticipants();

        while(rs.next()){
            Player player = Player.readPlayerData(rs);
            int tournamentID=rs.getInt(14);
            boolean betaald = rs.getBoolean(15);
            Optional <Tournament> optionalTournament=tournaments.stream().filter(t->t.getId()==tournamentID).findAny();
            if(optionalTournament.isPresent()){
                Tournament tournament=optionalTournament.get();
                tournament.getParticipants().add(new Deelname(player, betaald));
            }else throw new IllegalStateException();
        }

    }

    private void initTournamentList()throws SQLException{

        ResultSet rs = databaseConnection.executeQueryAndGetData(Q_ALLTOURNAMENTS);
        while(rs.next()){
            tournaments.add(Tournament.readTournament(rs));
        }

        addParticipants();

    }


}
