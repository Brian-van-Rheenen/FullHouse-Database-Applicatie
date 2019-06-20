package components;

import backend.DatabaseConnection;
import backend.TournamentProvider;
import components.panels.OverviewPanel;
import models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Optional;

public class ParticipantOverviewPanel extends OverviewPanel {


    private TournamentProvider tournamentProvider;
    private Tournament focusedToernooi;
    boolean gezocht = false;
    private TablePanel tablePanel;


    public ParticipantOverviewPanel() throws SQLException {

        this.tournamentProvider = new TournamentProvider();


        createButtons();

        tablePanel = new TablePanel(fetchDataModel());
        this.add(tablePanel, BorderLayout.CENTER);


    }

    private DefaultTableModel fetchDataModel() {
        DefaultTableModel res = new DefaultTableModel();
        String[] columns = {"ID", "Naam", "Betaald", "Postcode"};
        for (String column : columns) {
            res.addColumn(column);
        }

        return res;
    }

    @Override
    protected void createButtons() {
        JButton searchButton = new JButton("zoek toernooi");

        searchButton.addActionListener(e -> searchForEventAndFillTable());
        this.addButtonToPanel(searchButton);

        JButton filterButton = new JButton("Openstaande Betalingen");
        filterButton.addActionListener(e -> {
            if (gezocht = true) {

                //TODO
            } else {
                JOptionPane.showMessageDialog(this, "Er is nog geen toernooi ingevoerd om op te zoeken");
            }
        });

        this.addButtonToPanel(filterButton);

    }


    private void searchForEventAndFillTable() {
        DefaultTableModel defaultTableModel = fetchDataModel();

        String input = JOptionPane.showInputDialog(this, "Voer de code van het toernooi");

        Optional<Tournament> optionalToernooi = tournamentProvider.getTournaments()
                .stream()
                .filter(event -> event.isMatchForSearch(input)
                )
                .findAny();


        if (optionalToernooi.isPresent()) {
            gezocht = true;
            Tournament toernooi = optionalToernooi.get();
            focusedToernooi = toernooi;
            toernooi.getParticipants().forEach(deelname ->
                    defaultTableModel.addRow(deelname.getTableFormatData())

            );

            tablePanel.setModel(defaultTableModel);


        } else {
            JOptionPane.showMessageDialog(this, "Het systeem kon geen toernooi/masterclass vinden met de ingevulde gegevens");
        }
    }
}
