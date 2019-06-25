package backend;

import models.Mentor;

import java.sql.*;
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
                    "WHERE bs.deleted = FALSE\n" +
                    "ORDER BY bs.idBekend;";

    private static final String Q_CREATEMENTOR =
            "INSERT INTO bekende_speler (naam, telefoon, mail)\n" +
                    "VALUES (?, ?, ?);";

    private static final String Q_UPDATEMENTOR =
            "UPDATE bekende_speler\n" +
                    "SET naam     = ?,\n" +
                    "    telefoon = ?,\n" +
                    "    mail     = ?,\n" +
                    "    deleted  = ?\n" +
                    "WHERE idBekend = ?;";

    private static final String Q_DELETEMENTOR =
            "UPDATE bekende_speler\n" +
                    "SET naam = 'VERWIJDERD',\n" +
                    "    telefoon = 'VERWIJDERD',\n" +
                    "    mail = 'VERWIJDERD',\n" +
                    "    deleted = TRUE\n" +
                    "WHERE idBekend = ?;";

    private static final String Q_SELECTMENTOR =
            "SELECT bs.idBekend,\n" +
                    "       bs.naam,\n" +
                    "       bs.telefoon,\n" +
                    "       bs.mail,\n" +
                    "       bs.deleted\n" +
                    "FROM bekende_speler bs\n" +
                    "WHERE idBekend = ?\n" +
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

    public void updateMentor(Mentor updatedMentor) throws SQLException {

        PreparedStatement updateMentorStatement = getDatabaseConnection()
                .prepareStatement(Q_UPDATEMENTOR);

        int index = 0;
        updateMentorStatement.setString(++index, updatedMentor.getName());
        updateMentorStatement.setString(++index, updatedMentor.getPhoneNumber());
        updateMentorStatement.setString(++index, updatedMentor.getEmail());
        updateMentorStatement.setBoolean(++index, updatedMentor.isDeleted());

        updateMentorStatement.setInt(++index, updatedMentor.getId());
    }

    public void deleteMentor(int mentorId) throws SQLException {

        PreparedStatement deleteStatement = getDatabaseConnection()
                .prepareStatement(Q_DELETEMENTOR);

        int index = 0;
        deleteStatement.setInt(++index, mentorId);

        deleteStatement.executeUpdate();
    }

    public Mentor getMentorById(int mentorId) throws SQLException {

        PreparedStatement individualFetchStatement = getDatabaseConnection()
                .prepareStatement(Q_SELECTMENTOR);

        int index = 0;
        individualFetchStatement.setInt(++index, mentorId);

        ResultSet set = individualFetchStatement.executeQuery();
        set.next();

        return Mentor.fromResultSet(set);
    }
}
