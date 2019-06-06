package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class Player {

    private String name;

    private String gender;

    private String telephoneNR;
    private String email;
    private int rating;
    private String zip;
    private String woonplaats;
    private String street;
    private int houseNr;
    private String dobString;


    public Player(String name, int rating, String street,
                  int houseNr, String zip, String woonplaats,
                  String dob, String email, String telephoneNR, String gender) {
        this.street = street;
        this.rating = rating;
        this.houseNr = houseNr;
        this.zip = zip;
        this.name = name;
        this.gender = gender;
        this.woonplaats = woonplaats;
        this.dobString = dob;
        this.telephoneNR = telephoneNR;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Object[] convertToTableData() {
        Object[] res = {name, rating, street+" "+houseNr , zip, woonplaats, dobString, email, telephoneNR, gender};
        return res;
    }

    public static Player readPlayerData(ResultSet rs) throws SQLException {
        String name = rs.getString(1);
        int rating = rs.getInt(2);
        String street = rs.getString(3);
        int housenr = rs.getInt(4);
        String zip = rs.getString(5);
        String city = rs.getString(6);
        String dob = rs.getDate(7).toString();
        String mail = rs.getString(8);
        String tele = rs.getString(9);
        String geslacht = rs.getString(10);


        return new Player(name, rating, street, housenr, zip, city, dob, mail, tele, geslacht);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return houseNr == player.houseNr &&
                name.equals(player.name) &&
                telephoneNR.equals(player.telephoneNR) &&
                email.equals(player.email) &&
                street.equals(player.street) &&
                dobString.equals(player.dobString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, telephoneNR, email, street, houseNr, dobString);
    }

    public String getTelephoneNR() {
        return telephoneNR;
    }

    public String getEmail() {
        return email;
    }

    public int getRating() {
        return rating;
    }

    public String getZip() {
        return zip;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public String getStreet() {
        return street;
    }

    public int getHouseNr() {
        return houseNr;
    }

    public String getDobString() {
        return dobString;
    }
}
