package models;

import backend.SqlDateConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Tournament extends Event {

    private ArrayList<Deelname> participations = new ArrayList<>();
    private String theme;
    private Date finalSubmitDate;
    private String entryRestriction;

    public Tournament(int id, String city, int capacity, String startDate, Time startTime, String endDate, Time endTime, int entranceFee, String theme, String finalSubmitDate, String entryRestriction) {
        super(id, city, capacity, startDate, startTime, endDate, endTime, entranceFee);

        this.theme = theme;
        this.finalSubmitDate = SqlDateConverter.convertStringToSqlDate(finalSubmitDate);
        this.entryRestriction = entryRestriction;
    }

    public Tournament(String city, int capacity, String startDate, Time startTime, String endDate, Time endTime, int entranceFee, String theme, String finalSubmitDate, String entryRestriction) {
        super(city, capacity, startDate, startTime, endDate, endTime, entranceFee);

        this.theme = theme;
        this.finalSubmitDate = SqlDateConverter.convertStringToSqlDate(finalSubmitDate);
        this.entryRestriction = entryRestriction;
    }

    public boolean isMatchForSearch(String search) {
        boolean b = this.isOnSameDate(search) || this.theme.equalsIgnoreCase(search);
        System.out.println("boolean " + b);
        return b;
    }

    public static Tournament readTournament(ResultSet resultSet) throws SQLException {

        int index = 0;

        int id = resultSet.getInt(++index);
        String city = resultSet.getString(++index);
        int capacity = resultSet.getInt(++index);
        String startDate = resultSet.getString(++index);
        Time startTime = resultSet.getTime(++index);
        String endDate = resultSet.getString(++index);
        Time endTime = resultSet.getTime(++index);
        int entranceFee = resultSet.getInt(++index);

        String theme = resultSet.getString(++index);
        String finalSubmitDate = resultSet.getString(++index);
        String entryRestriction= resultSet.getString(++index);

        return new Tournament(id, city, capacity, startDate, startTime, endDate, endTime, entranceFee, theme, finalSubmitDate, entryRestriction);
    }

    @Override
    public Object[] convertToTableData() {
        return new Object[]{theme, super.getBasicFieldsEvent(), entryRestriction};
    }

    /**
     * @deprecated
     * Dit is niet de bedoeling! Gebruik de Tournamentprovider a.u.b.
     *
     */
    @Deprecated
    public ArrayList<Deelname> getParticipations() {
        return participations;
    }

    public String getEntryRestriction() {
        return entryRestriction;
    }

    public Date getFinalSubmitDate() {
        return finalSubmitDate;
    }

    public String getTheme() {
        return theme;
    }


}
