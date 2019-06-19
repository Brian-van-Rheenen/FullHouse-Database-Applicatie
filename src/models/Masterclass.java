package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Masterclass extends Event {

    private int id;
    private int capacity;
    private int minimumRating;
    private String mentor;

    public Masterclass(int id, String city, int capacity, String StartDate, Time StartTime, String endDate, Time endTime, int minimumRating, int entranceFee, String mentor) {
        super(id, city, StartDate, StartTime, endDate, endTime, entranceFee);

        this.id = id;
        this.capacity = capacity;
        this.minimumRating = minimumRating;
        this.mentor = mentor;
    }

    public static Masterclass readMasterclassData(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String city = rs.getString(2);
        int capacity = rs.getInt(3);
        String StartDate = rs.getString(4);
        Time StartTime = rs.getTime(5);
        String endDate = rs.getString(6);
        Time endTime = rs.getTime(7);
        int minimumRating = rs.getInt(8);
        int entranceFee = rs.getInt(9);
        String mentor = rs.getString(10);

        return new Masterclass(id, city, capacity, StartDate, StartTime, endDate, endTime, minimumRating, entranceFee, mentor);
    }

    /**
     * This method is not used in Masterclass, but the parent requires this method to be implemented.
     * If, for some reason, someone calls this method, throw an unsupported operation exception.
     * @param search String to be matched
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean isMatchForSearch(String search) {
        throw new UnsupportedOperationException();
    }

    public Object[] convertToTableData(){
        Object[] res = {id, getCity(), capacity, convertSqlDateToString(getStartDate()) + " " + getStartTime(), convertSqlDateToString(getEndDate()) + " " + getEndTime(), minimumRating, getEntranceFee(), mentor};
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
