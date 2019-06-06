package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class Player {

    private int id;
    private String name;
    private String gender;
    private Date dob;
    private String address;
    private String zip;
    private String city;
    private String telephoneNR;
    private String email;
    private int rating;

    public Player(int id, String name, String gender, Date dob, String address, String zip, String city, String telephoneNR, String email, int rating) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
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
        String adres = rs.getString(5);
        String zip = rs.getString(6);
        String city = rs.getString(7);
        String tele = rs.getString(8);
        String mail = rs.getString(9);
        int rating = rs.getInt(10);

        return new Player(id, name, gender, dob, adres, zip, city, tele, mail, rating);
    }

    public Object[] convertToTableData(){
        Object[] res = {id, name, gender, dob, address, zip, city, telephoneNR, email, rating};
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
