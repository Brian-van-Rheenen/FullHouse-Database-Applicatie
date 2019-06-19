package models;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
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

    public Event(int id, String stad, Date startDate, Time startTime, Date endDate, Time endTime, int fee) {
        this.id = id;
        this.city = stad;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.entranceFee = fee;
    }

    public Event(String stad, Date startDate, Time startTime, Date endDate, Time endTime, int entranceFee) {
        this(-1, stad, startDate, startTime, endDate, endTime, entranceFee);
    }

    public abstract Object[] getTableData();

    public Object[] getBasicFieldsEvent() {
        return new Object[]{city, startDate, startTime, endDate, endTime, entranceFee};
    }

    public ArrayList<Deelname> getParticipants() {
        return participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return city.equals(event.city) &&
                startDate.equals(event.startDate);
    }

    public abstract boolean isMatchForSearch(String search);

    public boolean isOnSameDate(String dateString) {
        return startDate.toString().equalsIgnoreCase(dateString);
    }

    public int getEntranceFee() {
        return entranceFee;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}
