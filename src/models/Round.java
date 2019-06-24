package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Round {

    private int id;
    private Tournament tournament;

    public Round(int id, Tournament tournament) {
        this.id = id;
        this.tournament = tournament;
    }

    public Round(Tournament tournament) {
        this.tournament = tournament;
    }

    public static Round fromResultSet(ResultSet resultSet, Tournament tournament) throws SQLException {

        int index = 0;

        int id = resultSet.getInt(++index);
        int tournamentID = resultSet.getInt(++index);

        if (tournamentID != tournament.getId())
            throw new SQLException("The given tournament does not match the foreign key ID");

        return new Round(id, tournament);
    }

    public int getId() {
        return id;
    }
}
