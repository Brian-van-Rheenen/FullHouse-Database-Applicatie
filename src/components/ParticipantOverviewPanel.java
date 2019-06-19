package components;

import backend.PlayerProvider;
import backend.TournamentProvider;
import models.*;
import models.Event;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ParticipantOverviewPanel extends JPanel {


    private JTextField searchField = new JTextField();
    private PlayerProvider playerProvider;
    private TournamentProvider tournamentProvider;

    private TablePanel comp;
    private ArrayList<Event> events = new ArrayList<>();

    private JPanel searchPanel = new JPanel();

    public ParticipantOverviewPanel() {
        this.playerProvider = new PlayerProvider();
        this.tournamentProvider = new TournamentProvider();

        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);

        BoxLayout boxLayout1 = new BoxLayout(searchPanel, BoxLayout.X_AXIS);
        searchPanel.setLayout(boxLayout1);

        JLabel label = new JLabel("Deelname Overzicht");
        label.setFont(new Font("Helvetica", Font.BOLD, 30));

        this.add(label);

        initSearchField();
        initButtons();
        initEventList();
        this.add(searchPanel);

        comp = new TablePanel(fetchDataModel());

        this.add(comp, BorderLayout.CENTER);


    }

    private DefaultTableModel fetchDataModel() {
        DefaultTableModel res = new DefaultTableModel();
        String[] columns = {"id", "Naam", "Geslacht", "Geboortedatum", "Adres", "Postcode", "Woonplaats", "Telefoon", "Email", "Rating", "Betaald"};
        for (String column : columns) {
            res.addColumn(column);
        }

        return res;
    }

    private void initEventList() {
        try {
            ArrayList<Tournament> toAdd = tournamentProvider.getTournaments();
            this.events.addAll(toAdd);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private void initSearchField() {

        searchField.setMaximumSize(new Dimension(500, 30));
        searchPanel.add(searchField);
    }

    private void initButtons() {

        JButton searchButton = new JButton("zoek toernooi");
        searchButton.addActionListener(e -> searchForEventAndFillTable());

        searchPanel.add(searchButton);

    }


    private void searchForEventAndFillTable() {
        String search = searchField.getText();
        Optional<Event> optionalEvent = events
                .stream()
                .filter(event -> event.isMatchForSearch(search)
                )
                .findAny();

        System.out.println("bestaat ie wel? " + optionalEvent.isPresent());

        if (optionalEvent.isPresent()){
            DefaultTableModel tableModel1 = fetchDataModel();
            optionalEvent.get().getParticipants().forEach(p->tableModel1.addRow(p.getTableFormatData()));
        }

        else {
            JOptionPane.showMessageDialog(this, "Het systeem kon geen toernooi/masterclass vinden met de ingevulde gegevens");
        }
    }
}
