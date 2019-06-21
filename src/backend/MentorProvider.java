package backend;

import models.Mentor;

import java.sql.PreparedStatement;
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

    private static final String Q_CREATEMENTOR =
            "INSERT INTO bekende_speler (naam, telefoon, mail)\n" +
                    "VALUES (?, ?, ?);";

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

    public Mentor addMentor(Mentor newMentor) throws SQLException {

        PreparedStatement insertMentorStatement = getDatabaseConnection()
                .prepareStatement(Q_CREATEMENTOR);

        int index = 0;
        insertMentorStatement.setString(++index, newMentor.getName());
        insertMentorStatement.setString(++index, newMentor.getPhoneNumber());
        insertMentorStatement.setString(++index, newMentor.getEmail());

        insertMentorStatement.executeUpdate();

        newMentor.setId(getLastInsertId());
        return newMentor;
    }
}
