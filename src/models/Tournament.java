package models;

import backend.SqlDateConverter;

import java.sql.*;
import java.time.LocalDate;

/**
 * Represents a Tournament organized by FullHouse
 */
public class Tournament extends Event {

    private String theme;
    private int totalDeposit;
    private Date finalSubmitDate;
    private String entryRestriction;

    /**
     * Constructor for constructing a database entry
     */
    private Tournament(int id, String city, int capacity, String startDate, Time startTime, String endDate, Time endTime, int entranceFee, String theme, int totalDeposit, String finalSubmitDate, String entryRestriction) {
        super(id, city, capacity, startDate, startTime, endDate, endTime, entranceFee);

        this.theme = theme;
        this.totalDeposit = totalDeposit;
        this.finalSubmitDate = SqlDateConverter.convertStringToSqlDate(finalSubmitDate);
        this.entryRestriction = entryRestriction;
    }

    /**
     * Create a new Tournament
     */
    public Tournament(String city, int capacity, String startDate, Time startTime, String endDate, Time endTime, int entranceFee, String theme, String finalSubmitDate, String entryRestriction) {
        super(city, capacity, startDate, startTime, endDate, endTime, entranceFee);

        this.theme = theme;
        // A new tournament has no deposits
        this.totalDeposit = 0;
        this.finalSubmitDate = SqlDateConverter.convertStringToSqlDate(finalSubmitDate);
        this.entryRestriction = entryRestriction;
    }

    /**
     * Parse a {@link Tournament} from the given {@link ResultSet}
     * @param resultSet the ResultSet to parse
     * @return a Fully constructed {@link Tournament}
     * @throws SQLException failed to parse the {@link ResultSet} to an {@link Tournament}
     */
    public static Tournament fromResultSet(ResultSet resultSet) throws SQLException {

        int index = 0;

        int id           = resultSet.getInt(++index);
        String city      = resultSet.getString(++index);
        int capacity     = resultSet.getInt(++index);
        String startDate = resultSet.getString(++index);
        Time startTime   = resultSet.getTime(++index);
        String endDate   = resultSet.getString(++index);
        Time endTime     = resultSet.getTime(++index);
        int entranceFee  = resultSet.getInt(++index);

        String theme            = resultSet.getString(++index);
        int totalDeposit        = resultSet.getInt(++index);
        String finalSubmitDate  = resultSet.getString(++index);
        String entryRestriction = resultSet.getString(++index);

        return new Tournament(id, city, capacity, startDate, startTime, endDate, endTime, entranceFee, theme, totalDeposit, finalSubmitDate, entryRestriction);
    }

    public boolean isMatchForSearch(String search) {
        return this.isOnSameDate(search) || this.theme.equalsIgnoreCase(search) || matchesID(search);
    }

    @Override
    public boolean checkEventDate() {
        LocalDate currentDate=LocalDate.now();
        LocalDate eventDate = this.getFinalSubmitDate().toLocalDate();
        return eventDate.isBefore(currentDate);
    }

    private boolean matchesID(String input) {
        if (!input.isEmpty() &&input.matches("^[0-9]*$")) {
            return Integer.parseInt(input) == this.getId();
        } else return false;
    }

    public String createDateTime(Date date, Time time) {
        return date + " " + time;
    }

    public String getEntryRestriction() {
        return entryRestriction;
    }

    public void setEntryRestriction(String entryRestriction) {
        this.entryRestriction = entryRestriction;
    }

    public java.sql.Date getFinalSubmitDate() {
        return finalSubmitDate;
    }

    public void setFinalSubmitDate(java.sql.Date finalSubmitDate) {
        this.finalSubmitDate = finalSubmitDate;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getTotalDeposit() {
        return totalDeposit;
    }
}
