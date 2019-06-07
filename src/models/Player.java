package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Player {

    private int id;
    private String name;
    private String gender;
    private java.sql.Date dob;
    private String telephoneNR;
    private String email;

    private int adresId;
    private String street;
    private int houseNr;
    private String zip;
    private String city;

    private int rating;

    public Player(int id, int adresId, String name, String gender, java.util.Date dob, String street, int houseNr, String zip, String city, String telephoneNR, String email, int rating) {
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
        int adresId = rs.getInt(2);
        String name = rs.getString(3);
        String gender = rs.getString(4);
        Date dob = rs.getDate(5);
        String street = rs.getString(6);
        int houseNr = rs.getInt(7);
        String zip = rs.getString(8);
        String city = rs.getString(9);
        String tele = rs.getString(10);
        String mail = rs.getString(11);
        int rating = rs.getInt(12);

        return new Player(id, adresId, name, gender, dob, street, houseNr, zip, city, tele, mail, rating);
    }

    public Object[] convertToTableData(){
        Object[] res = {id, name, gender, dob, street + " " + houseNr, zip, city, telephoneNR, email, rating};
        return res;
    }

    public static java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    public String convertSqlDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
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

    public void setId(int id) {
        this.id = id;
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

    public int getAdresId() {
        return adresId;
    }

    public int getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setTelephoneNR(String telephoneNR) {
        this.telephoneNR = telephoneNR;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAdresId(int adresId) {
        this.adresId = adresId;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouseNr(int houseNr) {
        this.houseNr = houseNr;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
