package components;

import backend.SqlDateConverter;
import backend.TournamentProvider;
import components.dialogs.AddTournamentDialog;
import components.dialogs.NoSelectionDialog;
import components.dialogs.exceptions.ExceptionDialog;
import components.panels.OverviewPanel;
import components.representation.*;
import models.Tournament;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Optional;

public class TournamentOverviewPanel extends OverviewPanel {

    private Representor<Tournament> tournamentRepresentor;
    private GenericTableModel<Tournament> model;
    private TournamentProvider tournamentProvider;
    private TablePanel tablePanel;

    public TournamentOverviewPanel() {
        tournamentProvider = new TournamentProvider();

        tournamentRepresentor = new RepresentationBuilder<Tournament>()
                .addColumn("ID"                      , Tournament::getId)
                .addColumn("Locatie"                 , Tournament::getCity)
                .addColumn("Capaciteit"              , Tournament::getCapacity)
                .addColumn("Begintijd"               , tournament -> SqlDateConverter.convertSqlDateToString(tournament.getStartDate()) + " " + tournament.getStartTime())
                .addColumn("Eindtijd"                , tournament -> SqlDateConverter.convertSqlDateToString(tournament.getEndDate()) + " " + tournament.getEndTime())
                .addColumn("Entreekosten"            , Tournament::getEntranceFee)
                .addColumn("Totale inleggeld"        , Tournament::getTotalDeposit)
                .addColumn("Thema"                   , Tournament::getTheme)
                .addColumn("Uiterste inschrijfdatum" , Tournament::getFinalSubmitDate)
                .addColumn("Toegangsbeperking"       , Tournament::getEntryRestriction)
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
            new ExceptionDialog("Er is een fout opgetreden bij het ophalen van alle toernooien.\nProbeer het opnieuw.");

            // Failed to download, replace it with an empty list
            model = new GenericTableModel<>(tournamentRepresentor);
        }
    }

    @Override
    protected void createButtons() {
        JButton addButton = new JButton("Toevoegen");
        addButton.setPreferredSize(new Dimension(150, 200));
        addButton.addActionListener(e -> {
            new AddTournamentDialog(model);
        });

        JButton editButton = new JButton("Wijzigen");
        editButton.setEnabled(false);
        editButton.addActionListener(e -> {

            if(tablePanel.getSelectedRows()  == null || tablePanel.getSelectedRows().length < 1) {
                new NoSelectionDialog("tournament");
            } else {
                int selectedRow = tablePanel.getSelectedRow();
                Optional<Tournament> updatingTournament = findTournamentInList((Integer) model.getValueAt(selectedRow, 0));
                if(!updatingTournament.isPresent()) {
                    // This suggests that we missed an event (delete or edit) and are out of sync with the database
                    // Refresh list with new data, although this should theoretically never happen
                    refreshTournamentData();
                    return;
                }

                // Update the masterclass
                new AddTournamentDialog(model, updatingTournament.get());
            }
        });
        editButton.setPreferredSize(new Dimension(150, 200));

        tablePanel.addSelectionListener((selectionEvent) -> {
            // Selection is not jet finished. Ignore the event
            if(selectionEvent.getValueIsAdjusting())
                return;

            int[] selectedRows = tablePanel.getSelectedRows();
            if(selectedRows.length == 1) {
                Tournament selectedTournament = model.get(selectedRows[0]);
                editButton.setEnabled(true);
                return;
            }

            // If we cannot find the masterclass or the selection count is not 1 disable the buttons
            editButton.setEnabled(false);
        });

        addButtonToPanel(addButton);
        addButtonToPanel(editButton);
    }

    private Optional<Tournament> findTournamentInList(int tournamentID) {
        return model.stream()
                .filter((tournament) -> tournament.getId() == tournamentID)
                .findFirst();
    }
}
