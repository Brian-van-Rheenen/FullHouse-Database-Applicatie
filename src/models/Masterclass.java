package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Masterclass extends Event {

    private int minimumRating;
    private String mentor;

    public Masterclass(int id, String city, int capacity, String StartDate, Time StartTime, String endDate, Time endTime, int entranceFee, int minimumRating,  String mentor) {
        super(id, city, capacity, StartDate, StartTime, endDate, endTime, entranceFee);

        this.minimumRating = minimumRating;
        this.mentor = mentor;
    }

    public Masterclass(String city, int capacity, String startDate, Time startTime, String endDate, Time endTime, int entranceFee, int minimumRating, String mentor) {
        super(city, capacity, startDate, startTime, endDate, endTime, entranceFee);

        this.minimumRating = minimumRating;
        this.mentor = mentor;
    }

    public static Masterclass readMasterclassData(ResultSet rs) throws SQLException {

        int index = 0;

        int id = rs.getInt(++index);
        String city = rs.getString(++index);
        int capacity = rs.getInt(++index);
        String StartDate = rs.getString(++index);
        Time StartTime = rs.getTime(++index);
        String endDate = rs.getString(++index);
        Time endTime = rs.getTime(++index);
        int entranceFee = rs.getInt(++index);

        int minimumRating = rs.getInt(++index);
        String mentor = rs.getString(++index);

        return new Masterclass(id, city, capacity, StartDate, StartTime, endDate, endTime, entranceFee, minimumRating,  mentor);
    }



    @Override
    public boolean isMatchForSearch(String search) {
        return Integer.toString(getId()).trim().equalsIgnoreCase(search);
    }

    public Object[] convertToTableData(){
        Object[] res = {getId(), getCity(), getCapacity(), convertSqlDateToString(getStartDate()) + " " + getStartTime(), convertSqlDateToString(getEndDate()) + " " + getEndTime(), minimumRating, getEntranceFee(), mentor};
        return res;
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
        return getCity().equals(masterclass.getCity()) &&
                getStartDate().equals(masterclass.getStartDate()) &&
                getStartTime().equals(masterclass.getStartTime()) &&
                getEndDate().equals(masterclass.getEndDate()) &&
                getEndTime() == masterclass.getEndTime() &&
                minimumRating == masterclass.minimumRating &&
                getEntranceFee() == masterclass.getEntranceFee() &&
                mentor.equals(masterclass.mentor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStartDate(), getStartTime(), getEndDate(), getEndTime(), minimumRating, getEntranceFee(), mentor);
    }

    public int getMinimumRating() {
        return minimumRating;
    }

    public void setMinimumRating(int minimumRating) {
        this.minimumRating = minimumRating;
    }

    public int getEntranceFee() {
        return super.getEntranceFee();
    }

    public void setEntranceFee(int entranceFee) {
        super.setEntranceFee(entranceFee);
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }
}
