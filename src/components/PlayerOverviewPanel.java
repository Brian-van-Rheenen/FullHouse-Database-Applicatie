package components;

import backend.PlayerProvider;
import backend.SqlDateConverter;
import components.dialogs.*;
import components.dialogs.exceptions.ExceptionDialog;
import components.panels.OverviewPanel;
import components.representation.*;
import models.Player;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Optional;

/**
 * This is the general overview panel for users
 */
public class PlayerOverviewPanel extends OverviewPanel {

    private Representor<Player> playerRepresentor;
    private GenericTableModel<Player> model;
    private PlayerProvider playerProvider;
    private TablePanel tablePanel;

    public PlayerOverviewPanel() {
        playerProvider = new PlayerProvider();

        playerRepresentor = new RepresentationBuilder<Player>()
            .addColumn("id"           , Player::getId)
            .addColumn("Naam"         , Player::getName)
            .addColumn("Geslacht"     , Player::getGender)
            .addColumn("Geboortedatum", player -> SqlDateConverter.convertSqlDateToString(player.getDob()))
            .addColumn("Adres"        , player -> String.format("%s %s", player.getAddress().getStreet(), player.getAddress().getHouseNr()))
            .addColumn("Postcode"     , player -> player.getAddress().getZipCode())
            .addColumn("Woonplaats"   , player -> player.getAddress().getCity())
            .addColumn("Telefoon"     , Player::getTelephoneNR)
            .addColumn("Email"        , Player::getEmail)
            .addColumn("Rating"       , Player::getRating)
            .build();

        downloadPlayerData();

        tablePanel = new TablePanel(model);
        this.add(tablePanel, BorderLayout.CENTER);
        createButtons();
    }

    /**
     * Refreshes the {@link #model} with data from the database
     */
    private void downloadPlayerData() {
        try {
            // Fill the table with SQL data
            model = new GenericTableModel<>(playerProvider.allPlayers(), playerRepresentor);
        } catch (SQLException e) {
            new ExceptionDialog("Er is een fout opgetreden bij het ophalen van alle spelers.");

            // Failed to download, replace it with an empty list
            model = new GenericTableModel<>(playerRepresentor);
        }
    }

    protected void createButtons() {
        JButton addButton = new JButton("Toevoegen");
        addButton.setPreferredSize(new Dimension(150, 200));
        addButton.addActionListener(e -> {
            new AddPlayerDialog(model);
        });

        JButton editButton = new JButton("Wijzigen");
        editButton.setEnabled(false);
        editButton.addActionListener(e -> {

            if(tablePanel.getSelectedRows() == null || tablePanel.getSelectedRows().length < 1) {
                new NoSelectionDialog("persoon");
            } else {
                int selectedRow = tablePanel.getSelectedRow();
                Optional<Player> updatingPlayer = findPlayerInList((Integer) model.getValueAt(selectedRow, 0));
                if(!updatingPlayer.isPresent()) {
                    // This suggests that we missed an event (delete or edit) and are out of sync with the database
                    // Refresh list with new data, although this should theoretically never happen
                    downloadPlayerData();
                    return;
                }

                // Update the player
                new AddPlayerDialog(model, updatingPlayer.get());
            }
        });
        editButton.setPreferredSize(new Dimension(150, 200));

        JButton deleteButton = new JButton("Verwijderen");
        deleteButton.setEnabled(false);
        deleteButton.setPreferredSize(new Dimension(150, 200));
        deleteButton.addActionListener(e -> {
            if(tablePanel.getSelectedRows()  == null || tablePanel.getSelectedRows().length < 1) {
                new NoSelectionDialog("persoon");
            } else {
                int playerId = (Integer) model.getValueAt(tablePanel.getSelectedRow(), 0);
                Optional<Player> possiblePlayer = findPlayerInList(playerId);

                if(possiblePlayer.isPresent() && model.indexOf(possiblePlayer.get()) != -1) {
                    Player player = possiblePlayer.get();
                    int playerIndex = model.indexOf(player);

                    // Code blocks until the Dialog is closed
                    int result = new DeleteDialog(playerId).showDialog();

                    if(result == JOptionPane.OK_OPTION) {
                        // Remove data from the model
                        try {
                            playerProvider.deletePlayer(playerId);
                            model.remove(playerIndex);
                            tablePanel.clearSelection();
                        } catch (SQLException exception) {
                            new ExceptionDialog("Er is een fout opgetreden bij het verwijderen van de speler.\nProbeer het opnieuw.");
                        }
                    }
                } else {
                    // This suggests that we missed an event (delete or edit) and are out of sync with the database
                    // Refresh list with new data, although this should theoretically never happen
                    downloadPlayerData();
                    JOptionPane.showMessageDialog(
                            this,
                            "De tabel loopt achter op de database. probeer de speler opnieuw te verwijderen",
                            "Foutmelding",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        tablePanel.addSelectionListener((selectionEvent) -> {
            // Selection is not jet finished. Ignore the event
            if(selectionEvent.getValueIsAdjusting())
                return;

            int[] selectedRows = tablePanel.getSelectedRows();
            if(selectedRows.length == 1) {
                Player selectedPlayer = model.get(selectedRows[0]);
                if(!selectedPlayer.isDeleted()) {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    return;
                }
            }

            // If we cannot find the player or the selection count is not 1 disable the buttons
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
        });

        addButtonToPanel(addButton);
        addButtonToPanel(editButton);
        addButtonToPanel(deleteButton);
    }

    private Optional<Player> findPlayerInList(int playerId) {
        return model.stream()
                .filter((player) -> player.getId() == playerId)
                .findFirst();
    }
}
