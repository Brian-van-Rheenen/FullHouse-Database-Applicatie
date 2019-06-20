package components;

import backend.TournamentProvider;
import components.panels.OverviewPanel;
import components.representation.GenericTableModel;
import components.representation.RepresentationBuilder;
import components.representation.Representor;
import models.Tournament;
import backend.SqlDateConverter;

import java.awt.*;
import java.sql.SQLException;

public class TournamentOverviewPanel extends OverviewPanel {

    private Representor<Tournament> tournamentRepresentor;
    private GenericTableModel<Tournament> model;
    private TournamentProvider tournamentProvider;
    private TablePanel tablePanel;

    public TournamentOverviewPanel() {
        tournamentProvider = new TournamentProvider();

        tournamentRepresentor = new RepresentationBuilder<Tournament>()
                .addColumn("ID", Tournament::getId)
                .addColumn("Locatie", Tournament::getCity)
                .addColumn("Capaciteit", Tournament::getCapacity)
                .addColumn("Begintijd", tournament -> SqlDateConverter.convertSqlDateToString(tournament.getStartDate()) + " " + tournament.getStartTime())
                .addColumn("Eindtijd", tournament -> SqlDateConverter.convertSqlDateToString(tournament.getEndDate()) + " " + tournament.getEndTime())
                .addColumn("Kosten", Tournament::getEntranceFee)
                .addColumn("Thema", Tournament::getTheme)
                .addColumn("Uiterste inschrijfdatum", Tournament::getFinalSubmitDate)
                .addColumn("Toegangsbeperking", Tournament::getEntryRestriction)
                .build();

        refreshTournamentData();

        tablePanel = new TablePanel(model);
        this.add(tablePanel, BorderLayout.CENTER);
        createButtons();
    }

    /**
     * Refreshes the {@link #model} with data from the database
     */
    private void refreshTournamentData() {
        try {
            // Fill the table with SQL data
            model = new GenericTableModel<>(tournamentProvider.getTournaments(), tournamentRepresentor);
        } catch (SQLException e) {
            e.printStackTrace();
            // Failed to download, replace it with an empty list
            model = new GenericTableModel<>(tournamentRepresentor);
        }
    }

    @Override
    protected void createButtons() {

    }
}
