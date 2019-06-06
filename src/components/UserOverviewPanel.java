package components;

import backend.DataGetter;
import components.dialogs.TestPlayerDialog;
import models.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * This is the general overview panel for users
 */
public class UserOverviewPanel extends JPanel {

    private DataGetter dataGetter;
    private ArrayList<Player> playerTableData = new ArrayList<>();


    public UserOverviewPanel() {


        JPanel leftMenuPanel = new JPanel(new GridBagLayout());
        leftMenuPanel.setBackground(Color.GREEN);

        DefaultTableModel model = new DefaultTableModel();

        try {
            dataGetter = new DataGetter();
            fillTable(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        setLookAndFeel();

        addLeftMenuButtons(leftMenuPanel);

        TablePanel tablePanel = new TablePanel(model);
        this.add(tablePanel, BorderLayout.CENTER);
    }

    private void setLookAndFeel() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new EtchedBorder()));
    }

    private void fillTable(DefaultTableModel tableModel) throws SQLException {
        Object[] columnNames = {"Naam", "Rating", "Adres","Postcode", "Stad", "Geboortedatum", "Email", "Telefoon", "Geslacht"};
        playerTableData.addAll(dataGetter.allPlayers());

        for (int i = 0; i < 8; i++) {
            tableModel.addColumn(columnNames[i]);

        }

        playerTableData.stream().forEach(player -> tableModel.addRow(player.convertToTableData()));


    }

    private void addLeftMenuButtons(JPanel leftMenuPanel) {
        JPanel leftMenuButtonPanel = getLeftMenuButtonsPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weighty = 1.0;

        leftMenuPanel.add(leftMenuButtonPanel, gbc);

        this.add(leftMenuPanel, BorderLayout.LINE_START);
    }

    private JPanel getLeftMenuButtonsPanel() {
        JPanel leftMenuButtonPanel = new JPanel(new GridLayout(3, 1, 20, 20));

        Dimension buttonDimension = new Dimension(150, 200);

        leftMenuButtonPanel.setPreferredSize(buttonDimension);


        JButton addButton = new JButton("Toevoegen");
        addButton.setPreferredSize(buttonDimension);

        addButton.addActionListener(event ->
                new TestPlayerDialog(this.playerTableData)
        );


        JButton editButton = new JButton("Wijzigen");
        editButton.addActionListener(e -> new TestPlayerDialog(this.playerTableData.get(5)));
        editButton.setPreferredSize(buttonDimension);

        JButton deleteButton = new JButton("Verwijderen");
        deleteButton.setPreferredSize(buttonDimension);

        leftMenuButtonPanel.add(addButton);
        leftMenuButtonPanel.add(editButton);
        leftMenuButtonPanel.add(deleteButton);
        return leftMenuButtonPanel;
    }
}
