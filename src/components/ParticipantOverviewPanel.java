package components;

import backend.MasterclassProvider;
import backend.ParticipantProvider;
import backend.TournamentProvider;
import components.dialogs.AddParticipantDialog;
import components.dialogs.reports.PaymentTableDialog;
import components.panels.OverviewPanel;
import models.Event;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ParticipantOverviewPanel extends OverviewPanel {


    private TournamentProvider tournamentProvider;
    private MasterclassProvider masterclassProvider;
    private Event focusedEvent;
    boolean isSearchPerformed = false;
    private TablePanel tablePanel;
    private ParticipantProvider participantProvider;

    public ParticipantOverviewPanel() {

        this.tournamentProvider = new TournamentProvider();
        this.participantProvider = new ParticipantProvider();
        this.masterclassProvider = new MasterclassProvider();

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
        JButton searchButton = new JButton("Zoek toernooi");

        searchButton.addActionListener(e -> {

            searchForEventAndFillTable();

        });
        this.addButtonToPanel(searchButton);

        JButton filterButton = new JButton("Nog niet betaald");
        filterButton.addActionListener(e ->
                showPaymentOverview()
        );

        JButton addParticipantButton = new JButton("Nieuwe deelname");
        addParticipantButton.addActionListener(a -> {
            AddParticipantDialog addParticpant = new AddParticipantDialog(false);

        });

        this.addButtonToPanel(filterButton);

    }

    private void showPaymentOverview() {
        try {
            if (isSearchPerformed = true) {

                PaymentTableDialog paymentTableDialog = new PaymentTableDialog(participantProvider.getPartcipants(focusedEvent));
                if (paymentTableDialog.hasChangedSomething()) {

                    submitPaidParticipations(paymentTableDialog);

                    refreshAndFillTable();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Er is nog geen toernooi ingevoerd om op te zoeken");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void submitPaidParticipations(PaymentTableDialog paymentTableDialog) {
        try {
            participantProvider.updatePaymentStatusParticipants(paymentTableDialog.getPaidParticipations(), focusedEvent);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Event> getAllEvents() throws SQLException {
        ArrayList<Event> events = new ArrayList<>();

        events.addAll(tournamentProvider.getTournaments());

        events.addAll(masterclassProvider.allMasterclasses());


        events.forEach(e -> System.out.println(e.getId()));
        return events;

    }


    private void searchForEventAndFillTable() {


        String input = JOptionPane.showInputDialog(this, "Voer de code van het event in");

        try {
            ArrayList<Event> events = getAllEvents();

            if (input != null) {

                Optional<Event> optionalEvent = events
                        .stream()
                        .filter(event -> event.isMatchForSearch(input)
                        )
                        .findAny();


                if (optionalEvent.isPresent()) {
                    System.out.println("gevondem");

                    isSearchPerformed = true;

                    focusedEvent = optionalEvent.get();
                    refreshAndFillTable();


                } else {
                    JOptionPane.showMessageDialog(this, "Het systeem kon geen event vinden met de ingevulde data");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshAndFillTable() {
        try {
            DefaultTableModel defaultTableModel = fetchDataModel();

            participantProvider.getPartcipants(focusedEvent).forEach(deelname ->
                    defaultTableModel.addRow(deelname.getTableFormatData())

            );

            tablePanel.setModel(defaultTableModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
