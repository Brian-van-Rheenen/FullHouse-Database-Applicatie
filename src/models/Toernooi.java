package models;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class Toernooi {

    private ArrayList <Deelname> participations = new ArrayList<>();
    private String themanaam;
    private Date date;
    private String code;

    public ArrayList<Deelname> getParticipations() {
        return participations;
    }

    public static Toernooi readTournament(ResultSet resultSet){
        return null;
    }

    public void addContestant(Player player){
        Deelname deelname = new Deelname(player, false);
        if (participations.stream().noneMatch(partici -> partici.getPlayer().equals(player))){
            participations.add(deelname);
        }
    }


    public String getThemanaam() {
        return themanaam;
    }

    public Date getDate() {
        return date;
    }

    public String getCode() {
        return code;
    }
}
