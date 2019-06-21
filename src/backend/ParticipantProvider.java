package backend;

import models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ParticipantProvider extends DatabaseProvider {

    private static final String Q_MASTERCLASS_PARTICPANTS="SELECT gast, a.adres_id, naam, geslacht, gebdatum, a.straatnaam, a.huisnummer," +
            " a.postcode,  a.woonplaats, telefoon, email, rating, deleted, mc_code, betaald " +
            "FROM masterclass_deelname " +
            "INNER JOIN speler on gast=speler_id " +
            "INNER JOIN masterclass on mc_code=idMasterclass " +
            "INNER JOIN adres a on speler.adres_id = a.adres_id " +
            "WHERE mc_code=?;";

    private static final String INSERT_MASTERCLASS_PARTICIPANT="INSERT INTO masterclass_deelname(gast, mc_code, betaald) VALUES(?,?,0)";
    private static final String INSERT_TOURNAMENT_PARTICIPANT="INSERT INTO toernooi_deelname(speler, toernooiCode, betaald) VALUES(?,?,0)";

    private static final String UPDATE_PAYMENT = "UPDATE toernooi_deelname SET betaald=1 WHERE speler=?";
    private static String UPDATE_PAYMENT_MASTERCLASS="UPDATE masterclass_deelname SET betaald=1 WHERE gast=?";

    private static final String Q_PARTCIPANTS_TOURNAMENT =
            "SELECT speler_id, a.adres_id, naam, geslacht, gebdatum, a.straatnaam, a.huisnummer," +
                    " a.postcode,  a.woonplaats, telefoon, email, rating, deleted, toernooiCode, betaald " +
                    "FROM toernooi_deelname " +
                    "INNER JOIN speler on toernooi_deelname.speler=speler_id " +
                    "INNER JOIN toernooi on toernooiCode=idToernooi " +
                    "INNER JOIN adres a on speler.adres_id = a.adres_id WHERE toernooiCode=?;";

    public boolean insertParticipant(Participant participant, Event event) throws SQLException{
        String toUse=INSERT_MASTERCLASS_PARTICIPANT;
        if(event instanceof Tournament){
            toUse=INSERT_TOURNAMENT_PARTICIPANT;
        }

        PreparedStatement preparedStatement =getDatabaseConnection().prepareStatement(toUse);
        preparedStatement.setInt(1, participant.getPlayer().getId());
        preparedStatement.setInt(2, event.getId());

        return  preparedStatement.execute();
    }

    public ArrayList<Participant> addParticipants(Event event) throws SQLException {

        ArrayList <Participant> participants = new ArrayList<>();


        ResultSet rs = queryParticipants(event);

        while(rs.next()){
            rs.getInt(14); //contains the idcode of the event (masterclass or tournament)
            boolean hasPaid = rs.getBoolean(15);
            participants.add(new Participant(Player.readPlayerData(rs), hasPaid));
        }

        event.getParticipants().clear();
        event.getParticipants().addAll(participants);

        return participants;

    }

    private ResultSet queryParticipants(Event event) throws SQLException {
        PreparedStatement preparedStatement = getDatabaseConnection().prepareStatement(Q_MASTERCLASS_PARTICPANTS);

        if(event instanceof Tournament){
            preparedStatement=getDatabaseConnection().prepareStatement(Q_PARTCIPANTS_TOURNAMENT);
        }

        preparedStatement.setInt(1, event.getId());
        return preparedStatement.executeQuery();
    }

    public void updatePaymentStatusParticipants(ArrayList<Participant> paidParticipations, Event event) throws SQLException {
        String toUse = UPDATE_PAYMENT;
        if(event instanceof Masterclass){
            toUse=UPDATE_PAYMENT_MASTERCLASS;
        }


        for (Participant p : paidParticipations) {
            PreparedStatement preparedStatement = getDatabaseConnection().prepareStatement(toUse);
            preparedStatement.setInt(1, p.getPlayer().getId());
            preparedStatement.executeUpdate();
        }
    }



}
