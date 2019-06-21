package components;

import backend.MasterclassProvider;
import backend.ParticipantProvider;
import backend.PlayerProvider;
import backend.TournamentProvider;
import components.dialogs.AddParticipantDialog;
import components.dialogs.reports.PaymentTableDialog;
import components.panels.OverviewPanel;
import models.Event;
import models.Participant;
import models.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ParticipantOverviewPanel extends OverviewPanel {


    private TournamentProvider tournamentProvider;
    private MasterclassProvider masterclassProvider;
    private PlayerProvider playerProvider;
    private Event focusedEvent;
    boolean isSearchPerformed = false;
    private TablePanel tablePanel;
    private ParticipantProvider participantProvider;

    public ParticipantOverviewPanel() {

        this.tournamentProvider = new TournamentProvider();
        this.participantProvider = new ParticipantProvider();
        this.masterclassProvider = new MasterclassProvider();
        this.playerProvider = new PlayerProvider();

        createButtons();

        tablePanel = new TablePanel(fetchDataModel());

        this.add(tablePanel, BorderLayout.CENTER);


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
            checkAndSubmitNewParticipant();


        });

        this.addButtonToPanel(addParticipantButton);

        this.addButtonToPanel(filterButton);

    }

    private void checkAndSubmitNewParticipant() {
        try {
            AddParticipantDialog addParticipantDialog = new AddParticipantDialog(false);

            Optional<Event> optionalEvent = searchEvents(addParticipantDialog.getInputForEvent(), getAllEvents());


            Optional<Player> optionalPlayer = playerProvider
                    .allPlayers()
                    .stream()
                    .filter(p -> Integer.toString(p.getId()).equalsIgnoreCase(addParticipantDialog.getInputForPlayer())).findAny();

            if (optionalEvent.isPresent() && optionalPlayer.isPresent()) {
                Event event = optionalEvent.get();
                focusedEvent=event;
                Player player=optionalPlayer.get();
                Participant participant=new Participant(player, false);

                participantProvider.addParticipants(event);

                if(event.hasParticipant(participant)){
                    JOptionPane.showMessageDialog(this, "De speler is al opgegeven voor dit event");
                }else{
                    participantProvider.insertParticipant(participant,event);
                    event.getParticipants().add(participant);
                    refreshAndFillTable();
                }

            }else JOptionPane.showMessageDialog(this, "Het systeem kon geen speler/event vinden met deze code");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList <Participant> notPaidList(){
        ArrayList <Participant> res = new ArrayList<>();

        focusedEvent.getParticipants().stream().filter(p->!p.hasPaid()).forEach(res::add);
        return  res;
    }


    private void showPaymentOverview() {

            if (focusedEvent!=null) {

                PaymentTableDialog paymentTableDialog = new PaymentTableDialog(notPaidList());
                if (paymentTableDialog.hasChangedSomething()) {

                    submitPaidParticipations(paymentTableDialog);

                    refreshAndFillTable();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Er is nog geen toernooi ingevoerd om op te zoeken");
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

                Optional<Event> optionalEvent = searchEvents(input, events);


                if (optionalEvent.isPresent()) {
                    System.out.println("gevondem");

                    isSearchPerformed = true;

                    focusedEvent = optionalEvent.get();
                    participantProvider.addParticipants(focusedEvent);

                    refreshAndFillTable();


                } else {
                    JOptionPane.showMessageDialog(this, "Het systeem kon geen event vinden met de ingevulde data");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Optional<Event> searchEvents(String input, ArrayList<Event> events) {
        Optional<Event> optionalEvent = events
                .stream()
                .filter(event -> event.isMatchForSearch(input)
                )
                .findAny();

        return optionalEvent;
    }

    private void refreshAndFillTable() {

            DefaultTableModel defaultTableModel = fetchDataModel();

            focusedEvent.getParticipants().forEach(deelname ->
                    defaultTableModel.addRow(deelname.getTableFormatData())

            );

            tablePanel.setModel(defaultTableModel);

    }

    private DefaultTableModel fetchDataModel() {
        DefaultTableModel res = new DefaultTableModel();
        String[] columns = {"ID", "Naam", "Betaald", "Postcode"};
        for (String column : columns) {
            res.addColumn(column);
        }


        return res;
    }
}
