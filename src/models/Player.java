package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Objects;

public class Player {

    private int id;
    private String name;
    private String gender;
    private java.sql.Date dob;
    private String street;
    private int houseNr;
    private String zip;
    private String city;
    private String telephoneNR;
    private String email;
    private int rating;

    public Player(int id, String name, String gender, java.util.Date dob, String street, int houseNr, String zip, String city, String telephoneNR, String email, int rating) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dob = convertJavaDateToSqlDate(dob);
        this.street = street;
        this.houseNr = houseNr;
        this.zip = zip;
        this.city = city;
        this.telephoneNR = telephoneNR;
        this.email = email;
        this.rating = rating;
    }

    public static Player readPlayerData(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String name = rs.getString(2);
        String gender = rs.getString(3);
        Date dob = rs.getDate(4);
        String street = rs.getString(5);
        int houseNr = rs.getInt(6);
        String zip = rs.getString(7);
        String city = rs.getString(8);
        String tele = rs.getString(9);
        String mail = rs.getString(10);
        int rating = rs.getInt(11);

        return new Player(id, name, gender, dob, street, houseNr, zip, city, tele, mail, rating);
    }

    public Object[] convertToTableData(){
        Object[] res = {id, name, gender, dob, street + " " + houseNr, zip, city, telephoneNR, email, rating};
        return res;
    }

    public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return name.equals(player.name) &&
                street.equals(player.street) &&
                houseNr == player.houseNr &&
                gender.equals(player.gender) &&
                dob.equals(player.dob) &&
                email.equals(player.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, street, houseNr, gender, dob, email);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Date getDob() {
        return dob;
    }

    public String getStreet() {
        return street;
    }

    public int getHouseNr() {
        return houseNr;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
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
