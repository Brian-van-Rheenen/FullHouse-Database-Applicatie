package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Masterclass {

    private int id;
    private String city;
    private Date date;
    private Time beginTime;
    private Time endTime;
    private int minimumRating;
    private int price;
    private String mentor;

    public Masterclass(int id, String city, String date, Time beginTime, Time endTime, int minimumRating, int price, String mentor) {
        this.id = id;
        this.city = city;
        this.date = convertStringToSqlDate(date);
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.minimumRating = minimumRating;
        this.price = price;
        this.mentor = mentor;
    }

    public static Masterclass readMasterclassData(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String city = rs.getString(2);
        String date = rs.getString(3);
        Time beginTime = rs.getTime(4);
        Time endTime = rs.getTime(5);
        int minimumRating = rs.getInt(6);
        int price = rs.getInt(7);
        String mentor = rs.getString(8);

        return new Masterclass(id, city, date, beginTime, endTime, minimumRating, price, mentor);
    }

    public Object[] convertToTableData(){
        Object[] res = {id, city, date, beginTime, endTime, minimumRating, price, mentor};
        return res;
    }

    public java.sql.Date convertStringToSqlDate(String dateString) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        java.util.Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new java.sql.Date(date.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Masterclass)) return false;
        Masterclass masterclass = (Masterclass) o;
        return city.equals(masterclass.city) &&
                beginTime.equals(masterclass.beginTime) &&
                date.equals(masterclass.date) &&
                endTime == masterclass.endTime &&
                minimumRating == masterclass.minimumRating &&
                price == masterclass.price &&
                mentor.equals(masterclass.mentor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, beginTime, date, endTime, minimumRating, price, mentor);
    }

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public Date getDate() {
        return date;
    }

    public Time getBeginTime() {
        return beginTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public int getMinimumRating() {
        return minimumRating;
    }

    public int getPrice() {
        return price;
    }

    public String getMentor() {
        return mentor;
    }
}
