package components;

import backend.PlayerProvider;
import components.dialogs.AddPlayerDialog;
import components.dialogs.DeleteDialog;
import components.dialogs.NoSelectionDialog;
import models.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This is the general overview panel for users
 */
public class UserOverviewPanel extends JPanel {

    private ArrayList<Player> playerTableData = new ArrayList<>();

    private DefaultTableModel model;
    private PlayerProvider playerProvider;
    private TablePanel tablePanel;

    public UserOverviewPanel() {

        playerProvider = new PlayerProvider();

        JPanel leftMenuPanel = new JPanel(new GridBagLayout());
        leftMenuPanel.setBackground(Color.LIGHT_GRAY);

        setLookAndFeel();

        addLeftMenuButtons(leftMenuPanel);

        model = fetchDataModel();
        tablePanel = new TablePanel(model);
        this.add(tablePanel, BorderLayout.CENTER);
    }

    private void setLookAndFeel() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private void addLeftMenuButtons(JPanel leftMenuPanel) {
        JPanel leftMenuButtonPanel = getLeftMenuButtonsPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weighty = 1.0;

        leftMenuPanel.add(leftMenuButtonPanel, gbc);

        this.add(leftMenuPanel, BorderLayout.LINE_START);
    }

    /**
     * Fetches all rows from the database and put's it in a model
     * @return an event with the updated model
     */
    private DefaultTableModel fetchDataModel() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            Object[] columnNames = {"id", "Naam", "Geslacht", "Geboortedatum", "Adres", "Postcode", "Woonplaats", "Telefoon", "Email", "Rating"};
            ArrayList<Player> players = playerProvider.allPlayers();

            for (Object columnName : columnNames) {
                model.addColumn(columnName);
            }

            players.forEach(player -> model.addRow(player.convertToTableData()));
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return model;
    }

    private JPanel getLeftMenuButtonsPanel() {
        JPanel leftMenuButtonPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        leftMenuButtonPanel.setPreferredSize(new Dimension(150, 200));
        leftMenuButtonPanel.setBackground(Color.LIGHT_GRAY);

        JButton addButton = new JButton("Toevoegen");
        addButton.setPreferredSize(new Dimension(150, 200));
        addButton.addActionListener(e -> {
            AddPlayerDialog dialog = new AddPlayerDialog(playerTableData);

            dialog.addListener((player) -> {
                DefaultTableModel model = (DefaultTableModel) tablePanel.getModel();
                model.addRow(player.convertToTableData());
            });
        });

        JButton editButton = new JButton("Wijzigen");
        editButton.addActionListener(e -> new AddPlayerDialog(this.playerTableData.get(5)));
        editButton.setPreferredSize(new Dimension(150, 200));

        JButton deleteButton = new JButton("Verwijderen");
        deleteButton.setPreferredSize(new Dimension(150, 200));
        deleteButton.addActionListener(e -> {
            if(tablePanel.getSelectedRows()  == null || tablePanel.getSelectedRows().length < 1) {
                new NoSelectionDialog();
            } else {
                int id = (Integer) model.getValueAt(tablePanel.getSelectedRow(), 0);
                // Code blocks until the Dialog is closed
                new DeleteDialog(id);
                // Refresh the data

                DefaultTableModel model = (DefaultTableModel) tablePanel.getModel();
                // model.removeRow();

                // tablePanel.updateModel(fetchDataModel());
            }
        });

        leftMenuButtonPanel.add(addButton);
        leftMenuButtonPanel.add(editButton);
        leftMenuButtonPanel.add(deleteButton);
        return leftMenuButtonPanel;
    }
}
