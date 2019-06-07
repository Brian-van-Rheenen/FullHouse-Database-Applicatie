package components;

import backend.MasterclassProvider;
import components.panels.OverviewPanel;
import models.Masterclass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This is the general overview panel for users
 */
public class MasterclassOverviewPanel extends OverviewPanel {

    private ArrayList<Masterclass> masterclassTableData = new ArrayList<>();

    private DefaultTableModel model;
    private MasterclassProvider masterclassProvider;
    private TablePanel tablePanel;

    public MasterclassOverviewPanel() {

        masterclassProvider = new MasterclassProvider();

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
            Object[] columnNames = {"id", "Stad", "Datum", "Begintijd", "Eindtijd", "Minimale Rating", "Kosten", "Begeleider"};
            ArrayList<Masterclass> masterclasses = masterclassProvider.allMasterclasses();

            for (Object columnName : columnNames) {
                model.addColumn(columnName);
            }

            masterclasses.forEach(masterclass -> model.addRow(masterclass.convertToTableData()));
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return model;
    }

    protected void createButtons() {
        JButton addButton = new JButton("Toevoegen");
        addButton.setPreferredSize(new Dimension(150, 200));
        addButton.addActionListener(e -> {
            //AddPlayerDialog dialog = new AddPlayerDialog(masterclassTableData);

            //dialog.addListener((nothing) -> tablePanel.updateModel(fetchDataModel()));
        });

        JButton editButton = new JButton("Wijzigen");
        //editButton.addActionListener(e -> new AddPlayerDialog(this.masterclassTableData.get(5)));
        editButton.setPreferredSize(new Dimension(150, 200));

        JButton reportButton = new JButton("Rapportages");
        reportButton.setPreferredSize(new Dimension(150, 200));

        addButtonToPanel(addButton);
        addButtonToPanel(editButton);
        addButtonToPanel(reportButton);
    }
}
