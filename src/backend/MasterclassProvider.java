package backend;

import models.viewmodels.MasterclassOverviewModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractList;
import java.util.ArrayList;

public class MasterclassProvider {
    private DatabaseConnection connection;

    public MasterclassProvider(DatabaseConnection connection) {
        this.connection = connection;
    }

    public AbstractList<MasterclassOverviewModel> getMasterclasses() throws SQLException {

        Statement statement = connection
                .getConnection()
                .createStatement();

        ResultSet set = statement.executeQuery(
            "SELECT l.stad                             AS Locatie,\n" +
                    "       begintijd AS Datum,\n" +
                    "       TIME_FORMAT(begintijd, '%H:%i')    AS Begintijd,\n" +
                    "       TIME_FORMAT(eindtijd, '%H:%i')     AS Eindtijd,\n" +
                    "       minimumLevel                       AS 'Minimale rating',\n" +
                    "       inschrijfgeld                      AS Kosten,\n" +
                    "       bs.naam                            AS Begeleider\n" +
                    "FROM event\n" +
                    "         INNER JOIN masterclass m on event.idEvent = m.idMasterclass\n" +
                    "         INNER JOIN locatie l on event.locatie = l.idLocatie\n" +
                    "         INNER JOIN bekende_speler bs on m.begeleiderNr = bs.idBekend\n" +
                    "ORDER BY m.idMasterclass;"
        );

        ArrayList<MasterclassOverviewModel> masterclassOverview = new ArrayList<>();

        while (set.next()) {

            MasterclassOverviewModel model = new MasterclassOverviewModel(
                    set.getString(1),
                    set.getDate(2),
                    set.getTime(3),
                    set.getTime(4),
                    set.getInt(5),
                    set.getDouble(6),
                    set.getString(7)
            );

            masterclassOverview.add(model);
        }


        return masterclassOverview;
    }
}
