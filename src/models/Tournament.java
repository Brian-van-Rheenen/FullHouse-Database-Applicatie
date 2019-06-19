package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Tournament extends Event {

    private ArrayList<Deelname> participations = new ArrayList<>();
    private String themanaam;
    private String entranceCheck;

    public static final String Q_ALLTOURNAMENTS = "SELECT l.stad                                           AS Locatie,\n" +
            "       DATE_FORMAT(begintijd, '%d-%m-%Y')               AS Begindatum,\n" +
            "       TIME_FORMAT(begintijd, '%H:%i')                  AS Begintijd,\n" +
            "       DATE_FORMAT(eindtijd, '%d-%m-%Y')                AS Einddatum,\n" +
            "       TIME_FORMAT(eindtijd, '%H:%i')                   AS Eindtijd,\n" +
            "       DATE_FORMAT(uiterste_inschrijfdatum, '%d-%m-%Y') AS 'Uiterste inschrijfdatum',\n" +
            "       thema                                            AS Thema,\n" +
            "       inschrijfgeld                                    AS Kosten,\n" +
            "       toegang_beperking                                AS Toegangsbeperking\n" +
            "FROM event\n" +
            "         INNER JOIN locatie l on event.locatie = l.idLocatie\n" +
            "         INNER JOIN toernooi t ON idEvent = t.idToernooi\n" +
            "ORDER BY t.idToernooi;";


    public Tournament(String stad, Date startDate, Time startTime, Date endDate, Time endTime, int fee , String themanaam, String entranceCheck) {
        super(stad, startDate, startTime, endDate, endTime, fee);
        this.entranceCheck = entranceCheck;
        this.themanaam = themanaam;
    }

    @Override
    public boolean isMatchForSearch(String search) {
        boolean b = this.isOnSameDate(search) || this.themanaam.equalsIgnoreCase(search);
        System.out.println("boolean " + b);
        return b;
    }

    public ArrayList<Deelname> getParticipations() {
        return participations;
    }

    public static Tournament readTournament(ResultSet resultSet) throws SQLException {

        String stad = resultSet.getString(1);
        Date startDate = resultSet.getDate(2);
        Time startTime = resultSet.getTime(3);
        Date endDate = resultSet.getDate(4);
        Time endTime = resultSet.getTime(5);
        Date maxSubmitDate = resultSet.getDate(6);
        String thema = resultSet.getString(7);

        int inschrijfgeld = resultSet.getInt(8);
        String entrance= resultSet.getString(9);

        return new Tournament(stad,startDate,startTime, endDate, endTime, inschrijfgeld,thema, entrance);
    }



    @Override
    public Object[] getTableData() {
        return new Object[]{themanaam, super.getBasicFieldsEvent(), entranceCheck};
    }

    public String getEntranceCheck() {
        return entranceCheck;
    }

    public String getThemanaam() {
        return themanaam;
    }


}
