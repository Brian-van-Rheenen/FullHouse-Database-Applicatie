package models;

import backend.SqlDateConverter;

import java.sql.*;
import java.util.Objects;

/**
 * Represents a person who has registered itself by FullHouse
 */
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

    /**
     * This constructor is only to be used while creating classes that represent database entries
     */
    private Player(int id, Address address, String name, Gender gender, java.util.Date dob, String telephoneNR, String email, int rating, boolean deleted) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dob = SqlDateConverter.convertJavaDateToSqlDate(dob);
        this.telephoneNR = telephoneNR;
        this.email = email;
        this.address = address;
        this.rating = rating;
        this.deleted = deleted;
    }

    /**
     * Constructor for creating new players that are not yet in the database
     */
    public Player(Address address, String name, Gender gender, java.util.Date dob, String telephoneNR, String email, int rating) {
        this(-1, address, name, gender, dob, telephoneNR, email, rating, false);
    }

    /**
     * Create a {@link Player} from a {@link ResultSet}
     *
     * @param rs the {@link ResultSet} to parse
     * @return a fully constructed {@link Player}
     * @throws SQLException failed to parse the player correctly
     */
    public static Player fromResultSet(ResultSet rs) throws SQLException {

        int index = 0;
        int id          = rs.getInt(++index);
        int adresId     = rs.getInt(++index);
        String name     = rs.getString(++index);
        Gender gender   = Gender.parse(rs.getString(++index));
        Date dob        = rs.getDate(++index);
        String street   = rs.getString(++index);
        int houseNr     = rs.getInt(++index);
        String zip      = rs.getString(++index);
        String city     = rs.getString(++index);
        String tele     = rs.getString(++index);
        String mail     = rs.getString(++index);
        int rating      = rs.getInt(++index);
        boolean deleted = rs.getBoolean(++index);

        return new Player(id, new Address(adresId, city, street, houseNr, zip), name, gender, dob, tele, mail, rating, deleted);
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
