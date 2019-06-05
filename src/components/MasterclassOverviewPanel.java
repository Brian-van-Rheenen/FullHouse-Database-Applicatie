package components;

import backend.DatabaseConnection;
import backend.MasterclassProvider;
import models.viewmodels.MasterclassOverviewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class MasterclassOverviewPanel extends JPanel {

    private MasterclassProvider provider;

    public MasterclassOverviewPanel() {
        this.setLayout(new BorderLayout());

        try {
            provider = new MasterclassProvider(new DatabaseConnection());
        } catch (SQLException e) {
            // Print stacktrace
            e.printStackTrace();

            // Notify the user
            failure("An error has occurred while connecting to the database!");
        }

        DefaultTableModel model;

        try {

            String[] collumnNames = new String[] {
                    "Stad",
                    "Datum",
                    "Starttijd",
                    "Eindtijd",
                    "Minimum niveau",
                    "Inschrijfkosten",
                    "Begeleider"
            };

            Object[][] databaseData = provider.getMasterclasses().stream()
                    .map(MasterclassOverviewModel::toArray)
                    .toArray(Object[][]::new);

            model = new DefaultTableModel(databaseData, collumnNames);
        } catch (SQLException e) {
            e.printStackTrace();
            model = new DefaultTableModel();
            failure("Failed to retrieve information from the database!");
        }

        TablePanel table = new TablePanel(model);

        add(table, BorderLayout.CENTER);
    }



    private void failure(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "FAILURE",
                JOptionPane.ERROR_MESSAGE
        );
    }

}
