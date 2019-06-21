package models;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a {@link Mentor} for {@link Masterclass}
 */
public class Mentor {

    private int id;
    private String name;
    private String phoneNumber;
    private String email;
    private boolean deleted;

    private Mentor(int id, String name, String phoneNumber, String email, boolean deleted) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.deleted = deleted;
    }

    public Mentor(String name, String phoneNumber, String email) {
        this(-1, name, phoneNumber, email, false);
    }

    public static Mentor fromResultSet(ResultSet set) throws SQLException {

        int index = 0;
        int id             = set.getInt(++index);
        String name        = set.getString(++index);
        String phoneNumber = set.getString(++index);
        String email       = set.getString(++index);
        boolean isDeleted  = set.getBoolean(++index);

        return new Mentor(id, name, phoneNumber, email, isDeleted);
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
