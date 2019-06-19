package models;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Event {

    private int id;
    private String city;
    private Date startDate;
    private Time startTime;
    private Date endDate;
    private Time endTime;
    private int entranceFee;
    private ArrayList<Deelname> participants = new ArrayList<>();

    public Event(int id, String city, String startDate, Time startTime, String endDate, Time endTime, int entranceFee) {
        this.id = id;
        this.city = city;
        this.startDate = convertStringToSqlDate(startDate);
        this.startTime = startTime;
        this.endDate = convertStringToSqlDate(endDate);
        this.endTime = endTime;
        this.entranceFee = entranceFee;
    }

    public Event(String city, String startDate, Time startTime, String endDate, Time endTime, int entranceFee) {
        this(-1, city, startDate, startTime, endDate, endTime, entranceFee);
    }

    public abstract boolean isMatchForSearch(String search);

    public abstract Object[] convertToTableData();

    public Object[] getBasicFieldsEvent() {
        return new Object[]{city, startDate, startTime, endDate, endTime, entranceFee};
    }

    public ArrayList<Deelname> getParticipants() {
        return participants;
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
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return city.equals(event.city) &&
                startDate.equals(event.startDate);
    }

    public boolean isOnSameDate(String dateString) {
        return startDate.toString().equalsIgnoreCase(dateString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, startDate);
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
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

    public int getEntranceFee() {
        return entranceFee;
    }

    public void setEntranceFee(int entranceFee) {
        this.entranceFee = entranceFee;
    }
}
