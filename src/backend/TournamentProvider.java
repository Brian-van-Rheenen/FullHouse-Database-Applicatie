package backend;

import models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class TournamentProvider extends DatabaseProvider {

    //region Queries

    private final String Q_ALLTOURNAMENTS =
            "        SELECT t.idToernooi                                       AS ID,\n" +
                    "       l.stad                                             AS Locatie,\n" +
                    "       e.capaciteit                                       AS Capaciteit,\n" +
                    "       DATE_FORMAT(e.begintijd, '%d-%m-%Y')               AS Begindatum,\n" +
                    "       TIME_FORMAT(e.begintijd, '%H:%i')                  AS Begintijd,\n" +
                    "       DATE_FORMAT(e.eindtijd, '%d-%m-%Y')                AS Einddatum,\n" +
                    "       TIME_FORMAT(e.eindtijd, '%H:%i')                   AS Eindtijd,\n" +
                    "       e.inschrijfgeld                                    AS Kosten,\n" +
                    "       t.thema                                            AS Thema,\n" +
                    "       (COUNT(td.speler) * e.inschrijfgeld)               AS Totaal_inschrijfgeld,\n" +
                    "       DATE_FORMAT(t.uiterste_inschrijfdatum, '%d-%m-%Y') AS 'Uiterste inschrijfdatum',\n" +
                    "       toegang_beperking                                  AS Toegangsbeperking\n" +
                    "FROM event e\n" +
                    "         INNER JOIN locatie l on e.locatie = l.idLocatie\n" +
                    "         INNER JOIN toernooi t ON idEvent = t.idToernooi\n" +
                    "         LEFT JOIN toernooi_deelname td on t.idToernooi = td.toernooiCode\n" +
                    "GROUP BY t.idToernooi;";

    private static final String Q_PARTCIPANTS =
            "SELECT speler_id, a.adres_id, naam, geslacht, gebdatum, a.straatnaam, a.huisnummer," +
                    " a.postcode,  a.woonplaats, telefoon, email, rating, deleted, toernooiCode, betaald " +
                    "FROM toernooi_deelname " +
                    "INNER JOIN speler on toernooi_deelname.speler=speler_id " +
                    "INNER JOIN toernooi on toernooiCode=idToernooi " +
                    "INNER JOIN adres a on speler.adres_id = a.adres_id ;";

    private static final String Q_PARTCIPANTS_TOURNAMENT =
            "SELECT speler_id, a.adres_id, naam, geslacht, gebdatum, a.straatnaam, a.huisnummer," +
                    " a.postcode,  a.woonplaats, telefoon, email, rating, deleted, toernooiCode, betaald " +
                    "FROM toernooi_deelname " +
                    "INNER JOIN speler on toernooi_deelname.speler=speler_id " +
                    "INNER JOIN toernooi on toernooiCode=idToernooi " +
                    "INNER JOIN adres a on speler.adres_id = a.adres_id where toernooiCode=?;";

    private static final String UPDATE_PAYMENT = "update toernooi_deelname set betaald=1 where speler=?";

    private static final String Q_ADDTOURNAMENT =
            "START TRANSACTION;\n" +
                    "INSERT INTO event (locatie, capaciteit, begintijd, eindtijd, inschrijfgeld)\n" +
                    "VALUES ((SELECT idLocatie FROM locatie WHERE stad = ? LIMIT 1), ?, ?, ?, ?);\n" +
                    "INSERT INTO toernooi (idToernooi, thema, uiterste_inschrijfdatum, toegang_beperking)\n" +
                    "VALUES (LAST_INSERT_ID(), ?, ?, ?);\n" +
                    "COMMIT;";

    private static final String Q_SELECTTOURNAMENT =
            "SELECT t.idToernooi,\n" +
                    "l.stad,\n" +
                    "       DATE_FORMAT(begintijd, '%d-%m-%Y'),\n" +
                    "       TIME_FORMAT(begintijd, '%H:%i'),\n" +
                    "       DATE_FORMAT(eindtijd, '%d-%m-%Y'),\n" +
                    "       TIME_FORMAT(eindtijd, '%H:%i'),\n" +
                    "       inschrijfgeld,\n" +
                    "       thema,\n" +
                    "       DATE_FORMAT(uiterste_inschrijfdatum, '%d-%m-%Y'),\n" +
                    "       toegang_beperking\n" +
                    "FROM event\n" +
                    "         INNER JOIN locatie l on event.locatie = l.idLocatie\n" +
                    "         INNER JOIN toernooi t ON idEvent = t.idToernooi\n" +
                    "WHERE idToernooi = ?;";

    private static final String Q_UPDATETOURNAMENT =
            "START TRANSACTION;\n" +
                    "UPDATE event\n" +
                    "SET locatie = (SELECT idLocatie FROM locatie WHERE stad = ?), capaciteit = ?, begintijd = ?, eindtijd = ?, inschrijfgeld = ?\n" +
                    "WHERE idEvent = ?;\n" +
                    "UPDATE toernooi\n" +
                    "SET thema = ?, uiterste_inschrijfdatum = ?, toegang_beperking = ?\n" +
                    "WHERE idToernooi = ?;\n" +
                    "COMMIT;";

    private static final String Q_UPCOMING_TOURNAMENT=
            "SELECT CONCAT(t.thema, ' toernooi')                AS Toernooi,\n" +
            "       CONCAT(l.stad)                              AS Locatie,\n" +
            "       DATE_FORMAT(begintijd, '%d-%m-%Y om %H:%i') AS Datum,\n" +
            "       COUNT(td.speler)                            AS `Alle inschrijvingen`,\n" +
            "       COALESCE(SUM(betaald = TRUE), 0)            AS `Voltooide betalingen`\n" +
            "FROM toernooi t\n" +
            "         LEFT JOIN toernooi_deelname td on t.idToernooi = td.toernooiCode\n" +
            "         JOIN event e on t.idToernooi = e.idEvent\n" +
            "         JOIN locatie l on e.locatie = l.idLocatie\n" +
            "WHERE DATE(e.begintijd) >= CURDATE()\n" +
            "GROUP BY t.idToernooi\n" +
            "ORDER BY e.begintijd ASC;";

    private static final String Q_ROUNDSPERTOURNAMENT =
                    "SELECT r.idRonde\n" +
                    "FROM toernooi t\n" +
                    "         LEFT JOIN ronde r on t.idToernooi = r.toernooi\n" +
                    "WHERE r.toernooi = ?\n" +
                    "ORDER BY r.idRonde ASC;";

    private static final String Q_TABLELAYOUTPERROUND =
                    "SELECT tf.idTafel,\n" +
                    "       s.naam,\n" +
                    "       s.rating\n" +
                    "FROM tafel_indeling ti\n" +
                    "         JOIN tafel tf ON ti.tafel = tf.idTafel\n" +
                    "         JOIN ronde r on ti.ronde = r.idRonde\n" +
                    "         JOIN toernooi t ON r.toernooi = t.idToernooi\n" +
                    "        JOIN speler s on ti.deelnemer = s.speler_id\n" +
                    "WHERE t.idToernooi = ?\n" +
                    "  AND ti.ronde = ?\n" +
                    "ORDER BY tf.idTafel, s.rating;";

    private static final String Q_ALLLOCATIONS =
            "SELECT stad FROM locatie;";

    //endregion Queries

    public ArrayList<Tournament> getTournaments() throws SQLException {
        ArrayList<Tournament> tournaments = new ArrayList<>();
        ResultSet rs = DatabaseConnection.executeQueryAndGetData(Q_ALLTOURNAMENTS);
        while (rs.next()) {
            tournaments.add(Tournament.fromResultSet(rs));
        }

        addParticipants(tournaments);

        return tournaments;
    }

    private ResultSet queryParticipants() throws SQLException {
        return getDatabaseConnection().prepareStatement(Q_PARTCIPANTS).executeQuery();
    }

    public ResultSet getUpcomingTournaments() {
        try {
            PreparedStatement preparedStatement = getDatabaseConnection().prepareStatement(Q_UPCOMING_TOURNAMENT);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not query upcoming tournaments");
        }
    }

    private void addParticipants(ArrayList<Tournament> tournaments) throws SQLException {
        ResultSet rs = queryParticipants();

        while (rs.next()) {
            Player player = Player.readPlayerData(rs);
            int tournamentID = rs.getInt(14);
            boolean paid = rs.getBoolean(15);
            Optional<Tournament> optionalTournament = tournaments.stream().filter(t -> t.getId() == tournamentID).findAny();
            if (optionalTournament.isPresent()) {
                Tournament tournament = optionalTournament.get();
                tournament.getParticipants().add(new Participant(player, paid));
            } else throw new IllegalStateException();
        }
    }

    /**
     * Add a new tournament to the database.
     *
     * @param tournament the tournament object to add.
     * @return Tournament object with an updated id
     * @throws SQLException
     */
    @SuppressWarnings("Duplicates")
    public Tournament addTournament(Tournament tournament) throws SQLException {
        PreparedStatement addTournamentStatement = getDatabaseConnection()
                .prepareStatement(Q_ADDTOURNAMENT, Statement.RETURN_GENERATED_KEYS);

        int index = 0;
        addTournamentStatement.setString(++index, tournament.getCity());
        addTournamentStatement.setInt(++index, tournament.getCapacity());
        addTournamentStatement.setString(++index, tournament.createDateTime(tournament.getStartDate(), tournament.getStartTime()));
        addTournamentStatement.setString(++index, tournament.createDateTime(tournament.getEndDate(), tournament.getEndTime()));
        addTournamentStatement.setInt(++index, tournament.getEntranceFee());

        if (tournament.getTheme().trim().isEmpty()) {
            addTournamentStatement.setString(++index, "Regulier");
            tournament.setTheme("Regulier");
        } else {
            addTournamentStatement.setString(++index, tournament.getTheme());
        }

        addTournamentStatement.setDate(++index, tournament.getFinalSubmitDate());

        if (tournament.getEntryRestriction().trim().isEmpty()) {
            addTournamentStatement.setString(++index, "Geen beperking");
            tournament.setEntryRestriction("Geen beperking");
        } else {
            addTournamentStatement.setString(++index, tournament.getEntryRestriction());
        }

        addTournamentStatement.executeUpdate();

        // Update the tournament with the generated id
        ResultSet set = getDatabaseConnection().createStatement().executeQuery("SELECT LAST_INSERT_ID()");
        if (set.next()) {
            // Set the ID for the tournament
            tournament.setId(set.getInt(1));
        }

        return tournament;
    }

    @SuppressWarnings("Duplicates")
    public void updateTournament(Tournament updated) throws SQLException {
        PreparedStatement updateTournamentStatement = getDatabaseConnection()
                .prepareStatement(Q_UPDATETOURNAMENT);

        int index = 0;
        updateTournamentStatement.setString(++index, updated.getCity());
        updateTournamentStatement.setInt(++index, updated.getCapacity());
        updateTournamentStatement.setString(++index, updated.createDateTime(updated.getStartDate(), updated.getStartTime()));
        updateTournamentStatement.setString(++index, updated.createDateTime(updated.getEndDate(), updated.getEndTime()));
        updateTournamentStatement.setInt(++index, updated.getEntranceFee());
        // Set the Event to update
        updateTournamentStatement.setInt(++index, updated.getId());

        if (updated.getTheme().trim().isEmpty()) {
            updateTournamentStatement.setString(++index, "Regulier");
            updated.setTheme("Regulier");
        } else {
            updateTournamentStatement.setString(++index, updated.getTheme());
        }

        updateTournamentStatement.setDate(++index, updated.getFinalSubmitDate());

        if (updated.getTheme().trim().isEmpty()) {
            updateTournamentStatement.setString(++index, "Geen beperking");
            updated.setEntryRestriction("Geen beperking");
        } else {
            updateTournamentStatement.setString(++index, updated.getEntryRestriction());
        }

        // Set the Masterclass to update
        updateTournamentStatement.setInt(++index, updated.getId());

        updateTournamentStatement.executeUpdate();
    }

    public ArrayList<Tournament> allTournaments() throws SQLException {
        ResultSet rs = DatabaseConnection.executeQueryAndGetData(Q_ALLTOURNAMENTS);
        ArrayList<Tournament> res = new ArrayList<>();

        while (rs.next()) {
            res.add(Tournament.fromResultSet(rs));
        }

        return res;
    }

    @SuppressWarnings("Duplicates")
    public ArrayList<String> getAllLocations() throws SQLException {
        ResultSet rs = DatabaseConnection.executeQueryAndGetData(Q_ALLLOCATIONS);
        ArrayList<String> res = new ArrayList<>();

        while (rs.next()) {
            res.add(rs.getString(1));
        }

        return res;
    }

    public ArrayList<Round> getRounds(Tournament tournament) throws SQLException {
        PreparedStatement getRounds = getDatabaseConnection()
                .prepareStatement(Q_ROUNDSPERTOURNAMENT);

        int index = 0;
        getRounds.setInt(++index, tournament.getId());

        ResultSet rs = getRounds.executeQuery();

        ArrayList<Round> res = new ArrayList<>();

        while (rs.next()) {
            res.add(Round.fromResultSet(rs, tournament));
        }

        return res;
    }

    public ArrayList<DesignatedTable> getTableLayout(Tournament selectedTournament, Round selectedRound) throws SQLException{
        PreparedStatement getTableLayout = getDatabaseConnection()
                .prepareStatement(Q_TABLELAYOUTPERROUND);

        int index = 0;

        getTableLayout.setInt(++index, selectedTournament.getId());
        getTableLayout.setInt(++index, selectedRound.getId());

        ResultSet rs = getTableLayout.executeQuery();

        ArrayList<DesignatedTable> res = new ArrayList<>();

        while (rs.next()) {
            res.add(DesignatedTable.fromResultSet(rs));
        }

        return res;
    }

}
