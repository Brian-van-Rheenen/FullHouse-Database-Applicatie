package models;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public abstract  class Event  {

    private String stad;
    private Date startDate;
    private Date endDate;
    private Time startTime;
    private  Time endTime;
    private int entranceFee;
    private ArrayList <Deelname> participants = new ArrayList<>();

    public Event(String stad, Date startDate, Time startTime, Date endDate, Time endTime, int fee) {
        this.entranceFee = fee;
        this.stad = stad;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public abstract Object [] getTableData();

    public Object [] getBasicFieldsEvent(){
        return new Object[]{stad, startDate, startTime, endDate, endTime, entranceFee};
    }

    public ArrayList<Deelname> getParticipants() {
        return participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return stad.equals(event.stad) &&
                startDate.equals(event.startDate);
    }

    public abstract boolean isMatchForSearch(String search);

    public boolean isOnSameDate(String dateString){
        return startDate.toString().equalsIgnoreCase(dateString);
    }

    public int getEntranceFee() {
        return entranceFee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stad, startDate);
    }

    public String getStad() {
        return stad;
    }

    public void setStad(String stad) {
        this.stad = stad;
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
