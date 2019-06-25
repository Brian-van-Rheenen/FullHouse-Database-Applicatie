package models;

import backend.SqlDateConverter;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents an event organized by FullHouse
 */
public abstract class Event {

    private int id;
    private String city;
    private int capacity;
    private Date startDate;
    private Time startTime;
    private Date endDate;
    private Time endTime;
    private int entranceFee;
    private ArrayList<Participant> participants = new ArrayList<>();

    protected Event(int id, String city, int capacity, String startDate, Time startTime, String endDate, Time endTime, int entranceFee) {
        this.id = id;
        this.capacity = capacity;
        this.city = city;
        this.startDate = SqlDateConverter.convertStringToSqlDate(startDate);
        this.startTime = startTime;
        this.endDate = SqlDateConverter.convertStringToSqlDate(endDate);
        this.endTime = endTime;
        this.entranceFee = entranceFee;
    }

    public Event(String city, int capacity, String startDate, Time startTime, String endDate, Time endTime, int entranceFee) {
        this(-1, city, capacity, startDate, startTime, endDate, endTime, entranceFee);
    }

    public boolean checkEventDate(){
        return false;
    }

    public abstract boolean isMatchForSearch(String search);

    public boolean isOnSameDate(String dateString) {
        return startDate.toString().equalsIgnoreCase(dateString);
    }

    public boolean hasParticipant(Participant participant) {
        return participants.stream().anyMatch(p->p.getPlayer().getId() == participant.getPlayer().getId());
    }

    public ArrayList<Participant> getParticipants() {
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
