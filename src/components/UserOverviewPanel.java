package components;

import backend.PlayerProvider;
import components.dialogs.AddPlayerDialog;
import components.dialogs.DeleteDialog;
import components.dialogs.NoSelectionDialog;
import components.panels.OverviewPanel;
import models.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This is the general overview panel for users
 */
public class UserOverviewPanel extends OverviewPanel {

    private ArrayList<Player> playerTableData = new ArrayList<>();

    private DefaultTableModel model;
    private PlayerProvider playerProvider;
    private TablePanel tablePanel;

    public UserOverviewPanel() {

        playerProvider = new PlayerProvider();

        createButtons();

        model = fetchDataModel();
        tablePanel = new TablePanel(model);
        this.add(tablePanel, BorderLayout.CENTER);
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

    protected void createButtons() {
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

        addButtonToPanel(addButton);
        addButtonToPanel(editButton);
        addButtonToPanel(deleteButton);
    }
}
