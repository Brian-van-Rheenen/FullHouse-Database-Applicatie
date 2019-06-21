package backend;

import models.Mentor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MentorProvider extends DatabaseProvider {

    // region Queries

    private static final String Q_ALLMENTORS =
            "SELECT bs.idBekend,\n" +
                    "       bs.naam,\n" +
                    "       bs.telefoon,\n" +
                    "       bs.mail,\n" +
                    "       bs.deleted\n" +
                    "FROM bekende_speler bs\n" +
                    "ORDER BY bs.idBekend;";

    // endregion Queries

    public ArrayList<Mentor> getAllMentors() throws SQLException {
        ResultSet set = getDatabaseConnection()
                .createStatement()
                .executeQuery(Q_ALLMENTORS);

        ArrayList<Mentor> mentors = new ArrayList<>();

        while (set.next()) {
            mentors.add(Mentor.fromResultSet(set));
        }

        return mentors;
    }

}
