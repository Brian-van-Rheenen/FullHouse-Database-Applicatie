package models.viewmodels;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MasterclassOverviewModel {

    public String location;
    public Date date;
    public Time startTime;
    public Time endTime;
    public int requiredRating;
    public double cost;
    public String teacher;

    public MasterclassOverviewModel(String location, Date date, Time startTime, Time endTime, int requiredRating, double cost, String teacher) {
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.requiredRating = requiredRating;
        this.cost = cost;
        this.teacher = teacher;
    }



    public Object[] toArray() {

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        return new Object[] {
                location,
                format.format(date),
                startTime,
                endTime,
                requiredRating,
                cost,
                teacher
        };
    }
}
