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
import java.util.List;

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

        try {
            playerTableData = playerProvider.allPlayers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        model = fetchDataModel(playerTableData);
        tablePanel = new TablePanel(model);
        this.add(tablePanel, BorderLayout.CENTER);
    }

    /**
     * Converts all Players to a dataModel
     * @return an event with the updated model
     */
    private DefaultTableModel fetchDataModel(List<Player> players) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Object[] columnNames = {"id", "Naam", "Geslacht", "Geboortedatum", "Adres", "Postcode", "Woonplaats", "Telefoon", "Email", "Rating"};

        for (Object columnName : columnNames) {
            model.addColumn(columnName);
        }

        players.forEach(player -> model.addRow(player.convertToTableData()));

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
        editButton.addActionListener(e -> {

            if(tablePanel.getSelectedRows()  == null || tablePanel.getSelectedRows().length < 1) {
                new NoSelectionDialog();
            } else {
                int selectedRow = tablePanel.getSelectedRow();
                Player updatingPlayer = findPlayerInList((Integer) model.getValueAt(tablePanel.getSelectedRow(), 0));
                if(updatingPlayer == null) {
                    // TODO: Refresh list with new data, although this should theoretically never happen
                    return;
                }

                AddPlayerDialog updateDialog = new AddPlayerDialog(updatingPlayer);
                updateDialog.addListener((player) -> {
                    // Refresh the data
                    DefaultTableModel model = (DefaultTableModel) tablePanel.getModel();
                    try {
                        model.removeRow(selectedRow);
                        Player result = playerProvider.getPlayerById(updatingPlayer.getId());

                        model.insertRow(selectedRow, result.convertToTableData());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        });
        editButton.setPreferredSize(new Dimension(150, 200));

        JButton deleteButton = new JButton("Verwijderen");
        deleteButton.setPreferredSize(new Dimension(150, 200));
        deleteButton.addActionListener(e -> {
            if(tablePanel.getSelectedRows()  == null || tablePanel.getSelectedRows().length < 1) {
                new NoSelectionDialog();
            } else {
                int id = (Integer) model.getValueAt(tablePanel.getSelectedRow(), 0);
                int selectedRow = tablePanel.getSelectedRow();
                // Code blocks until the Dialog is closed
                new DeleteDialog(id);
                // Refresh the data
                DefaultTableModel model = (DefaultTableModel) tablePanel.getModel();
                try {
                    model.removeRow(selectedRow);
                    model.insertRow(selectedRow, playerProvider.getPlayerById(id).convertToTableData());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        addButtonToPanel(addButton);
        addButtonToPanel(editButton);
        addButtonToPanel(deleteButton);
    }

    private Player findPlayerInList(int playerId) {
        for (Player p : playerTableData) {
            if(p.getId() == playerId)
                return p;
        }

        return null;
    }
}
