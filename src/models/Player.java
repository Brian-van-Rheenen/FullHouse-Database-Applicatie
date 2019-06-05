package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class Player {

    private String name;
    private String address;
    private String gender;
    private Date dob;
    private String telephoneNR;
    private String email;
    private int rating;
    private String zip;
    private String woonplaats;

    public Player(String name, int rating, String address,String zip, String woonplaats, String gender, String telephoneNR, String email,Date dob) {
        this.zip = zip;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.woonplaats = woonplaats;
        this.dob = dob;
        System.out.println(dob);
        this.rating = rating;
        this.telephoneNR = telephoneNR;
        this.email = email;
    }

    public static Player readPlayerData(ResultSet rs) throws SQLException {
        String name = rs.getString(1);
        int rating = rs.getInt(2);
        Date dob = rs.getDate(3);
        String adres = rs.getString(4);
        String zip = rs.getString(5);
        String city = rs.getString(6);
        String tele = rs.getString(7);
        String mail = rs.getString(8);
        String geslacht = rs.getString(9);

        return new Player(name, rating, adres, zip,city, geslacht, tele, mail, dob);
    }

    public Object[] convertToTableData(){
        Object[] res = {name, rating,  dob, gender, address, zip, telephoneNR, email};
        return res;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return name.equals(player.name) &&
                address.equals(player.address) &&
                gender.equals(player.gender) &&
                dob.equals(player.dob) &&
                email.equals(player.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, gender, dob, email);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public Date getDob() {
        return dob;
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
}
