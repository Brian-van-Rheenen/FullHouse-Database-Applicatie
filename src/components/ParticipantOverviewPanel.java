package components;

import backend.TournamentProvider;
import components.dialogs.reports.PaymentTableDialog;
import components.panels.OverviewPanel;
import models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Optional;

public class ParticipantOverviewPanel extends OverviewPanel {


    private TournamentProvider tournamentProvider;
    private Tournament focusedTournament;
    boolean isSearchPerformed = false;
    private TablePanel tablePanel;


    public ParticipantOverviewPanel() {

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

        searchButton.addActionListener(e -> {
            try {
                searchForEventAndFillTable();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        this.addButtonToPanel(searchButton);

        JButton filterButton = new JButton("Openstaande Betalingen");
        filterButton.addActionListener(e -> {
            if (isSearchPerformed = true) {

                PaymentTableDialog paymentTableDialog = new PaymentTableDialog(focusedTournament);
                if(paymentTableDialog.hasChangedSomething()){
                    refreshTable();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Er is nog geen toernooi ingevoerd om op te zoeken");
            }
        });

        this.addButtonToPanel(filterButton);

    }


    private void searchForEventAndFillTable() throws SQLException {


        String input = JOptionPane.showInputDialog(this, "Voer de code van het toernooi in");

        if (input != null) {

            Optional<Tournament> optionalToernooi = tournamentProvider.getTournaments()
                    .stream()
                    .filter(event -> event.isMatchForSearch(input)
                    )
                    .findAny();


            if (optionalToernooi.isPresent()) {
                isSearchPerformed = true;
                Tournament toernooi = optionalToernooi.get();
                focusedTournament = toernooi;
                refreshTable();




            } else {
                JOptionPane.showMessageDialog(this, "Het systeem kon geen toernooi/masterclass vinden met de ingevulde gegevens");
            }
        }
    }

    private void refreshTable() {
        DefaultTableModel defaultTableModel=fetchDataModel();
        try {
            tournamentProvider.participants(focusedTournament).forEach(deelname ->
                    defaultTableModel.addRow(deelname.getTableFormatData())

            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tablePanel.setModel(defaultTableModel);
    }
}
