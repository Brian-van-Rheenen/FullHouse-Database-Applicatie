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
    private int capacity;
    private Date beginDate;
    private Time beginTime;
    private Date endDate;
    private Time endTime;
    private int minimumRating;
    private int price;
    private String mentor;
    private int mentorId;

    public Masterclass(int id, String city, int capacity, String beginDate, Time beginTime, String endDate, Time endTime, int minimumRating, int price, String mentor, int mentorId) {
        this.id = id;
        this.city = city;
        this.capacity = capacity;
        this.beginDate = convertStringToSqlDate(beginDate);
        this.beginTime = beginTime;
        this.endDate = convertStringToSqlDate(endDate);
        this.endTime = endTime;
        this.minimumRating = minimumRating;
        this.price = price;
        this.mentor = mentor;
        this.mentorId = mentorId;
    }

    public static Masterclass readMasterclassData(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String city = rs.getString(2);
        int capacity = rs.getInt(3);
        String beginDate = rs.getString(4);
        Time beginTime = rs.getTime(5);
        String endDate = rs.getString(6);
        Time endTime = rs.getTime(7);
        int minimumRating = rs.getInt(8);
        int price = rs.getInt(9);
        String mentor = rs.getString(10);
        int mentorId = rs.getInt(11);

        return new Masterclass(id, city, capacity, beginDate, beginTime, endDate, endTime, minimumRating, price, mentor, mentorId);
    }

    public Object[] convertToTableData(){
        Object[] res = {id, city, capacity, convertSqlDateToString(beginDate) + " " + beginTime, convertSqlDateToString(endDate) + " " + endTime, minimumRating, price, mentor};
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

    public String convertSqlDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    public String createDateTime(Date date, Time time) {
        return date + " " + time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Masterclass)) return false;
        Masterclass masterclass = (Masterclass) o;
        return city.equals(masterclass.city) &&
                beginDate.equals(masterclass.beginDate) &&
                beginTime.equals(masterclass.beginTime) &&
                endDate.equals(masterclass.endDate) &&
                endTime == masterclass.endTime &&
                minimumRating == masterclass.minimumRating &&
                price == masterclass.price &&
                mentor.equals(masterclass.mentor) &&
                mentorId == masterclass.mentorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, beginDate, beginTime, endDate, endTime, minimumRating, price, mentor, mentorId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Time getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Time beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public int getMinimumRating() {
        return minimumRating;
    }

    public void setMinimumRating(int minimumRating) {
        this.minimumRating = minimumRating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }

    public int getMentorId() {
        return mentorId;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }
}
