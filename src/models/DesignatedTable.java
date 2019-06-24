package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DesignatedTable {

    private int tableId;
    private String playerName;
    private int playerRating;

    private DesignatedTable(int tableId, String playerName, int playerRating) {
        this.tableId = tableId;
        this.playerName = playerName;
        this.playerRating = playerRating;
    }

    public static DesignatedTable fromResultSet(ResultSet set) throws SQLException {

        int index = 0;
        int tableId       = set.getInt(++index);
        String playerName = set.getString(++index);
        int rating        = set.getInt(++index);

        return new DesignatedTable(tableId, playerName, rating);
    }

    public int getTableId() {
        return tableId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerRating() {
        return playerRating;
    }
}
