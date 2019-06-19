package components;

import backend.MasterclassProvider;
import components.dialogs.AddMasterclassDialog;
import components.dialogs.NoSelectionDialog;
import components.dialogs.reports.MasterclassFilterPlayerDialog;
import components.panels.OverviewPanel;
import components.representation.GenericTableModel;
import components.representation.RepresentationBuilder;
import components.representation.Representor;
import models.Masterclass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * This is the general overview panel for users
 */
public class MasterclassOverviewPanel extends OverviewPanel {

    private Representor<Masterclass> masterclassRepresentor;
    private GenericTableModel<Masterclass> model;
    private MasterclassProvider masterclassProvider;
    private TablePanel tablePanel;

    public MasterclassOverviewPanel() {
        masterclassProvider = new MasterclassProvider();

        masterclassRepresentor = new RepresentationBuilder<Masterclass>()
                .addColumn("id"                  , Masterclass::getId)
                .addColumn("Stad"                , Masterclass::getCity)
                .addColumn("Capaciteit"          , Masterclass::getCapacity)
                .addColumn("Begintijd"           , masterclass -> masterclass.convertSqlDateToString(masterclass.getBeginDate()) + " " + masterclass.getBeginTime())
                .addColumn("Eindtijd"            , masterclass -> masterclass.convertSqlDateToString(masterclass.getEndDate()) + " " + masterclass.getEndTime())
                .addColumn("Minimale Rating"     , Masterclass::getMinimumRating)
                .addColumn("Kosten"              , Masterclass::getPrice)
                .addColumn("Begeleider"          , Masterclass::getMentor)
                .build();

        downloadMasterclassData();

        tablePanel = new TablePanel(model);
        this.add(tablePanel, BorderLayout.CENTER);
        createButtons();
    }

    /**
     * Refreshes the {@link #model} with data from the database
     */
    private void downloadMasterclassData() {
        try {
            // Fill the table with SQL data
            model = new GenericTableModel<>(masterclassProvider.allMasterclasses(), masterclassRepresentor);
        } catch (SQLException e) {
            e.printStackTrace();
            // Failed to download, replace it with an empty list
            model = new GenericTableModel<>(masterclassRepresentor);
        }
    }

    protected void createButtons() {
        JButton addButton = new JButton("Toevoegen");
        addButton.setPreferredSize(new Dimension(150, 200));
        addButton.addActionListener(e -> {
            new AddMasterclassDialog(model);
        });

        JButton editButton = new JButton("Wijzigen");
        editButton.setEnabled(false);
        editButton.addActionListener(e -> {

            if(tablePanel.getSelectedRows()  == null || tablePanel.getSelectedRows().length < 1) {
                new NoSelectionDialog("masterclass");
            } else {
                int selectedRow = tablePanel.getSelectedRow();
                Optional<Masterclass> updatingMasterclass = findMasterclassInList((Integer) model.getValueAt(selectedRow, 0));
                if(!updatingMasterclass.isPresent()) {
                    // This suggests that we missed an event (delete or edit) and are out of sync with the database
                    // Refresh list with new data, although this should theoretically never happen
                    downloadMasterclassData();
                    return;
                }

                // Update the masterclass
                new AddMasterclassDialog(model, updatingMasterclass.get());
            }
        });
        editButton.setPreferredSize(new Dimension(150, 200));

        JButton filterButton = new JButton("Filter spelers");
        filterButton.setPreferredSize(new Dimension(150, 200));
        filterButton.addActionListener(e -> new MasterclassFilterPlayerDialog());

        tablePanel.addSelectionListener((selectionEvent) -> {
            // Selection is not jet finished. Ignore the event
            if(selectionEvent.getValueIsAdjusting())
                return;

            int[] selectedRows = tablePanel.getSelectedRows();
            if(selectedRows.length == 1) {
                Masterclass selectedMasterclass = model.get(selectedRows[0]);
                editButton.setEnabled(true);
                return;
            }

            // If we cannot find the masterclass or the selection count is not 1 disable the buttons
            editButton.setEnabled(false);
        });

        addButtonToPanel(addButton);
        addButtonToPanel(editButton);
        addButtonToPanel(filterButton);
    }

    private Optional<Masterclass> findMasterclassInList(int masterclassId) {
        return model.stream()
                .filter((masterclass) -> masterclass.getId() == masterclassId)
                .findFirst();
    }
}
