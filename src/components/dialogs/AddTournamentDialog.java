package components.dialogs;

import backend.SqlDateConverter;
import backend.TournamentProvider;
import components.dialogs.exceptions.ExceptionDialog;
import models.Tournament;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class AddTournamentDialog extends BasicDialog {

    private List<Tournament> tournamentList;
    private TournamentProvider provider = new TournamentProvider();
    private Tournament updatingTournament = null;

    private String[] locations = addLocations();
    private JComboBox locationField = new JComboBox(locations);

    private JTextField capacityField = new JTextField();

    private JTextField startDateField = new JTextField();
    private JTextField startTimeField = new JTextField();

    private JTextField endDateField = new JTextField();
    private JTextField endTimeField = new JTextField();

    private JTextField entranceFeeField = new JTextField();

    private JTextField themeField = new JTextField();

    private JTextField finalSubmitDateField = new JTextField();

    private JTextField entryRestrictionField = new JTextField();

    private JComponent[] fields = {locationField, capacityField, startDateField, startTimeField, endDateField, endTimeField, entranceFeeField, themeField, finalSubmitDateField, entryRestrictionField};

    public AddTournamentDialog(List<Tournament> tournaments) {
        super(false);
        tournamentList = tournaments;
        initChildDialog();
    }

    private void initChildDialog() {
        this.setSize(new Dimension(500, 800));

        addAllFields();
        addButtons();
        this.setVisible(true);
    }

    public AddTournamentDialog(List<Tournament> tournaments, Tournament toChange) {
        super(true);

        tournamentList = tournaments;
        updatingTournament = toChange;

        for (int i = 0; i < locations.length; i++) {
            if (locations[i].equals(toChange.getCity())) {
                locationField.setSelectedIndex(i);
            }
        }

        capacityField.setText(Integer.toString(toChange.getCapacity()));
        startDateField.setText(SqlDateConverter.convertSqlDateToString(toChange.getStartDate()));
        startTimeField.setText(toChange.getStartTime().toString());
        endDateField.setText(SqlDateConverter.convertSqlDateToString(toChange.getEndDate()));
        endTimeField.setText(toChange.getEndTime().toString());
        entranceFeeField.setText(Integer.toString(toChange.getEntranceFee()));
        themeField.setText(toChange.getTheme());
        finalSubmitDateField.setText(SqlDateConverter.convertSqlDateToString(toChange.getFinalSubmitDate()));
        entryRestrictionField.setText(toChange.getEntryRestriction());

        initChildDialog();
    }

    @Override
    public void handleConfirm() {
        if (!validateInput()) {
            JOptionPane.showMessageDialog(this, "Er zijn foute gegevens ingevoerd!");
            return;
        }

        if (!this.isForChange()) {
            try {
                // Attempt to add to the database, get the updated tournament with id back
                Tournament newTournament = provider.addTournament(createNewTournament());

                this.tournamentList.add(newTournament);
                JOptionPane.showMessageDialog(this, "De gegevens zijn opgeslagen.");
                this.dispose();
            } catch (SQLException e) {
                new ExceptionDialog("Er is een fout opgetreden en het is niet gelukt om het toernooi toe te voegen.");
            }
        } else {
            // Update the tournament in the database
            try {
                // Fetch updates from the screen
                Tournament updatedTournament = fetchUpdatesForTournament(updatingTournament);
                provider.updateTournament(updatedTournament);

                // Update the model
                int index = tournamentList.indexOf(updatedTournament);
                if (index == -1) {
                    // Could not find the model, something went wrong
                    // Recover by refreshing the entire list
                    tournamentList.clear();
                    tournamentList.addAll(provider.allTournaments());
                } else {
                    // Only update the player in the list
                    tournamentList.set(index, updatedTournament);
                }

                // Close the screen
                this.dispose();
            } catch (SQLException e) {
                new ExceptionDialog("Er is een fout opgetreden en het is niet gelukt om het toernooi aan te passen.");
            }
        }

    }

    private Tournament createNewTournament() {
        String location = (String) locationField.getSelectedItem();
        int capacity = Integer.parseInt(capacityField.getText());
        String startDate = startDateField.getText();
        Time startTime = java.sql.Time.valueOf(startTimeField.getText());
        String endDate = endDateField.getText();
        Time endTime = java.sql.Time.valueOf(endTimeField.getText());
        int entranceFee = Integer.parseInt(entranceFeeField.getText());
        String theme = themeField.getText();
        String finalSubmitDate = finalSubmitDateField.getText();
        String entryRestriction = entryRestrictionField.getText();

        return new Tournament(location, capacity, startDate, startTime, endDate, endTime, entranceFee, theme, finalSubmitDate, entryRestriction);
    }

    private Tournament fetchUpdatesForTournament(Tournament tournament) {

        tournament.setCity((String) locationField.getSelectedItem());
        tournament.setCapacity(Integer.parseInt(capacityField.getText()));
        tournament.setStartDate(SqlDateConverter.convertStringToSqlDate(startDateField.getText()));

        tournament.setStartTime(java.sql.Time.valueOf(startTimeField.getText()));
        tournament.setEndDate(SqlDateConverter.convertStringToSqlDate(endDateField.getText()));
        tournament.setEndTime(java.sql.Time.valueOf(endTimeField.getText()));
        tournament.setEntranceFee(Integer.parseInt(entranceFeeField.getText()));

        tournament.setTheme(themeField.getText());
        tournament.setFinalSubmitDate(SqlDateConverter.convertStringToSqlDate(finalSubmitDateField.getText()));
        tournament.setEntryRestriction(entryRestrictionField.getText());

        return tournament;
    }

    private boolean validateInput() {

        InputType[] tournamentDataTypes = {
                InputType.NUMBER,   // Capaciteit
                InputType.DATE,     // Start datum
                InputType.TIME,     // Starttijd
                InputType.DATE,     // Eind datun
                InputType.TIME,     // Eindtijd
                InputType.NUMBER,   // Inschrijfgeld / prijs
                                    // Thema
                InputType.DATE      // Uiterste inschrijfdatum
                                    // Toegangsbeperking
        };

        JTextField[] textFields = new JTextField[] {
                capacityField,
                startDateField,
                startTimeField,
                endDateField,
                endTimeField,
                entranceFeeField,
                finalSubmitDateField
                };

        return super.validateInput(tournamentDataTypes, textFields);
    }

    public void addAllFields() {
        String[] fieldnames = {
                "Locatie",
                "Capaciteit",
                "Startdatum",
                "Starttijd",
                "Einddatum",
                "Eindtijd",
                "Inschrijfgeld",
                "Thema",
                "Uiterste inschrijfdatum",
                "Toegangsbeperking"
        };

        super.addAllFields(fields, fieldnames);
    }

    private String[] addLocations() {
        try {
            ArrayList<String> locationsList = provider.getAllLocations();
            return locationsList.toArray(new String[locationsList.size()]);
        } catch (SQLException e) {
            new ExceptionDialog("Er is een fout opgetreden bij het ophalen van alle locaties.\nProbeer het opnieuw.");
        }

        return null;
    }

}
