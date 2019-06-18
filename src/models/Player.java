package models;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Player {

    private int id;

    private Address address;

    private String name;
    private Gender gender;
    private java.sql.Date dob;
    private String telephoneNR;
    private String email;

    private int rating;

    private boolean deleted;

    public Player(int id, Address address, String name, Gender gender, java.util.Date dob, String telephoneNR, String email, int rating, boolean deleted) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dob = convertJavaDateToSqlDate(dob);
        this.telephoneNR = telephoneNR;
        this.email = email;
        this.address = address;
        this.rating = rating;
        this.deleted = deleted;
    }

    public Player(Address address, String name, Gender gender, java.util.Date dob, String telephoneNR, String email, int rating) {
        this(-1, address, name, gender, dob, telephoneNR, email, rating, false);
    }

    public static Player readPlayerData(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        int adresId = rs.getInt(2);
        String name = rs.getString(3);
        Gender gender = Gender.parse(rs.getString(4));
        Date dob = rs.getDate(5);
        String street = rs.getString(6);
        int houseNr = rs.getInt(7);
        String zip = rs.getString(8);
        String city = rs.getString(9);
        String tele = rs.getString(10);
        String mail = rs.getString(11);
        int rating = rs.getInt(12);
        boolean deleted = rs.getBoolean(13);

        return new Player(id, new Address(adresId, city, street, houseNr, zip), name, gender, dob, tele, mail, rating, deleted);
    }

    public Object[] convertToTableData(){
        return new Object[] {
                id,
                name,
                gender,
                dob,
                address.getStreet() + " " + address.getHouseNr(),
                address.getZipCode(),
                address.getCity(),
                telephoneNR,
                email,
                rating
        };
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
                address.equals(player.address) &&
                gender.equals(player.gender) &&
                dob.equals(player.dob) &&
                email.equals(player.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, gender, dob, email);
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

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getTelephoneNR() {
        return telephoneNR;
    }

    public void setTelephoneNR(String telephoneNR) {
        this.telephoneNR = telephoneNR;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
