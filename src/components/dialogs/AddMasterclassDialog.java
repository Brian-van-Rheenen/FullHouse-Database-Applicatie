package components.dialogs;

import backend.MasterclassProvider;
import models.Masterclass;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class AddMasterclassDialog extends BasicDialog {

    private List<Masterclass> masterclassList;
    private MasterclassProvider provider = new MasterclassProvider();
    private Masterclass updatingMasterclass = null;

    private String[] locations = addLocations();
    private JComboBox locationField = new JComboBox(locations);

    private JTextField capacityField = new JTextField();

    private JTextField startDateField = new JTextField();
    private JTextField startTimeField = new JTextField();

    private JTextField endDateField = new JTextField();
    private JTextField endTimeField = new JTextField();

    private JTextField priceField = new JTextField();

    private JTextField minimumRatingField = new JTextField();

    private String[] famousPlayers = addFamousPlayers();
    private JComboBox mentorField = new JComboBox(famousPlayers);

    private JComponent[] fields = {locationField, capacityField, startDateField, startTimeField, endDateField, endTimeField, priceField, minimumRatingField, mentorField};


    public AddMasterclassDialog(List<Masterclass> masterclasses) {
        super(false);
        masterclassList = masterclasses;
        initChildDialog();
    }

    private void initChildDialog() {
        this.setSize(new Dimension(500, 800));

        addAllFields();
        addButtons();
        this.setVisible(true);
    }

    public AddMasterclassDialog(List<Masterclass> masterclasses, Masterclass toChange) {
        super(true);

        masterclassList = masterclasses;
        updatingMasterclass = toChange;

        for (int i = 0; i < locations.length; i++) {
            if(locations[i].equals(toChange.getCity())) {
                locationField.setSelectedIndex(i);
            }
        }

        capacityField.setText(Integer.toString(toChange.getCapacity()));
        startDateField.setText(toChange.convertSqlDateToString(toChange.getBeginDate()));
        startTimeField.setText(toChange.getBeginTime().toString());
        endDateField.setText(toChange.convertSqlDateToString(toChange.getEndDate()));
        endTimeField.setText(toChange.getEndTime().toString());
        priceField.setText(Integer.toString(toChange.getPrice()));
        minimumRatingField.setText(Integer.toString(toChange.getMinimumRating()));

        for (int i = 0; i < famousPlayers.length; i++) {
            if(famousPlayers[i].equals(toChange.getMentor())) {
                mentorField.setSelectedIndex(i);
            }
        }

        initChildDialog();
    }

    @Override
    public void handleConfirm() {
        // Check if the input is valid
        if (!validateInput()) {
            JOptionPane.showMessageDialog(this, "Er zijn foute gegevens ingevoerd!");
            return;
        }

        if (!this.isForChange()) {

            try {
                // Attempt to add to the database, get the updated masterclass with id back
                Masterclass newMasterclass = provider.addMasterclass(createNewMasterclass());

                this.masterclassList.add(newMasterclass);
                JOptionPane.showMessageDialog(this, "De gegevens zijn opgeslagen.");
                this.dispose();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Er is een fout opgetreden");
            }
        } else {
            // Update the masterclass in the database
            try {
                // Fetch updates from the screen
                Masterclass updatedMasterclass = fetchUpdatesForMasterclass(updatingMasterclass);
                provider.updateMasterclass(updatedMasterclass);

                // Update the model
                int index = masterclassList.indexOf(updatedMasterclass);
                if(index == -1) {
                    // Could not find the model, something went wrong
                    // Recover by refreshing the entire list
                    masterclassList.clear();
                    masterclassList.addAll(provider.allMasterclasses());
                } else {
                    // Only update the player in the list
                    masterclassList.set(index, updatedMasterclass);
                }

                // Close the screen
                this.dispose();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Er zijn foute gegevens ingevoerd!");
            }
        }
    }

    private Masterclass createNewMasterclass() {
        String location = (String) locationField.getSelectedItem();
        int capacity = Integer.parseInt(capacityField.getText());
        String startDate = startDateField.getText();
        Time startTime = java.sql.Time.valueOf(startTimeField.getText());
        String endDate = endDateField.getText();
        Time endTime = java.sql.Time.valueOf(endTimeField.getText());
        int price = Integer.parseInt(priceField.getText());
        int minimumRating = Integer.parseInt(minimumRatingField.getText());
        String mentor = (String) mentorField.getSelectedItem();

        return new Masterclass(0, location, capacity, startDate, startTime, endDate, endTime, minimumRating, price, mentor);
    }

    private Masterclass fetchUpdatesForMasterclass(Masterclass masterclass) {

        masterclass.setCity((String) locationField.getSelectedItem());
        masterclass.setCapacity(Integer.parseInt(capacityField.getText()));
        masterclass.setBeginDate(masterclass.convertStringToSqlDate(startDateField.getText()));

        masterclass.setBeginTime(java.sql.Time.valueOf(startTimeField.getText()));
        masterclass.setEndDate(masterclass.convertStringToSqlDate(endDateField.getText()));
        masterclass.setEndTime(java.sql.Time.valueOf(endTimeField.getText()));
        masterclass.setPrice(Integer.parseInt(priceField.getText()));

        masterclass.setMinimumRating(Integer.parseInt(minimumRatingField.getText()));
        masterclass.setMentor((String) mentorField.getSelectedItem());

        return masterclass;
    }

    private boolean validateInput() {

        InputType[] masterclassDataTypes = {
                InputType.CAPACITY, // Capaciteit
                InputType.DATE,     // Start datum
                InputType.TIME,     // Starttijd
                InputType.DATE,     // Eind datun
                InputType.TIME,     // Eindtijd
                InputType.NUMBER,   // Inschrijfgeld / prijs
                InputType.NUMBER,   // Minimale rating
        };

        JTextField[] textFields = new JTextField[]{capacityField, startDateField, startTimeField, endDateField, endTimeField, priceField, minimumRatingField};
        boolean res = true;

        for (int i = 0; i < masterclassDataTypes.length; i++) {
            JTextField textField = textFields[i];
            String input = textField.getText();
            boolean goodInput = masterclassDataTypes[i].isGoodInput(input);

            if (goodInput) {
                unmark(textField);

            } else {
                mark(textField);
                res = false;
            }
        }

        return res;
    }

    @Override
    public void addAllFields() {
        String[] fieldnames = {"Locatie", "Capaciteit", "Startdatum", "Starttijd", "Einddatum", "Eindtijd",
                "Inschrijfgeld",
                "Minimale Rating",
                "Mentor"};

        int nrOfFields = fields.length;

        for (int i = 0; i < nrOfFields; i++) {

            JLabel label = new JLabel(fieldnames[i]);
            label.setFont(new Font("Helvetica", Font.BOLD, 12));

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(label);

            this.add(panel);

            JComponent field = fields[i];
            field.setFont(new Font("Helvetica", Font.PLAIN, 20));
            this.add(field);
            this.add(Box.createRigidArea(new Dimension(300, 9)));
        }
    }

    private String[] addLocations() {
        try {
            ArrayList<String> locationsList = provider.getAllLocations();
            return locationsList.toArray(new String[locationsList.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String[] addFamousPlayers() {
        try {
            ArrayList<String> famousPlayersList = provider.getAllFamousPlayers();
            return famousPlayersList.toArray(new String[famousPlayersList.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
