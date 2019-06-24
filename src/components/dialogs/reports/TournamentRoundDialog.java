package components.dialogs.reports;

import backend.TournamentProvider;
import components.TablePanel;
import components.representation.*;
import models.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class TournamentRoundDialog extends JDialog {

    private TournamentProvider provider = new TournamentProvider();
    private ArrayList<Round> rounds;
    private ArrayList<DesignatedTable> tables = new ArrayList<>();

    public TournamentRoundDialog(Tournament tourmanent) {

        // Get the rounds for the Tournament
        try {
            rounds = provider.getRounds(tourmanent);
        } catch (SQLException ex) {
            ex.printStackTrace();
            rounds = new ArrayList<>();

            JOptionPane.showMessageDialog(this, "Er is iets foutgegaan tijdens het downloaden van gegevens", "Foutmelding",  JOptionPane.WARNING_MESSAGE);
            // Close the panel
            this.dispose();
        }

        setLayout(new BorderLayout());

        Representor<Round> representor = new RepresentationBuilder<Round>()
                .addColumn("Ronde", Round::getId)
                .build();

        GenericTableModel<Round> model = new GenericTableModel<>(rounds, representor);
        TablePanel tournamentRoundsOverview = new TablePanel(model);

        add(tournamentRoundsOverview, BorderLayout.WEST);

        // Details of table layout

        Representor<DesignatedTable> tableRepresentor = new RepresentationBuilder<DesignatedTable>()
                .addColumn("Tafel", DesignatedTable::getTableId)
                .addColumn("Speler", DesignatedTable::getPlayerName)
                .addColumn("Rating", DesignatedTable::getPlayerRating)
                .build();

        GenericTableModel<DesignatedTable> detailsModel = new GenericTableModel<>(tables, tableRepresentor);

        TablePanel roundDetails = new TablePanel(detailsModel);

        tournamentRoundsOverview.addSelectionListener((event) -> {
            if(event.getValueIsAdjusting())
                return;

            int selectedRow = tournamentRoundsOverview.getSelectedRow();
            if(selectedRow != -1) {
                Round selectedRound = model.get(selectedRow);

                try {
                    ArrayList<DesignatedTable> tables = provider.getTableLayout(tourmanent, selectedRound);
                    roundDetails.setModel(new GenericTableModel<>(tables, tableRepresentor));
                } catch (SQLException e) {
                    e.printStackTrace();
                    roundDetails.setModel(new GenericTableModel<>(tableRepresentor));
                }
            }
        });

        // roundDetails.add(new JLabel("Selecteer een ronde om de tafelindeling te zien", SwingConstants.CENTER));
        add(roundDetails, BorderLayout.CENTER);

        this.setPreferredSize(new Dimension(800, 600));
        this.pack();
        this.setVisible(true);
    }

}
