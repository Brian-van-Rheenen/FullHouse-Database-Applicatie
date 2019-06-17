package components.dialogs;

import backend.MasterclassProvider;
import models.Masterclass;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.function.Consumer;

public class AddMasterclassDialog extends BasicDialog {

    private ArrayList<Masterclass> masterclassList;
    private MasterclassProvider provider = new MasterclassProvider();
    private Masterclass updatingMasterclass = null;

    private JTextField locationField = new JTextField();

    private JTextField capacityField = new JTextField();

    private JTextField startDateField = new JTextField();
    private JTextField startTimeField = new JTextField();

    private JTextField endDateField = new JTextField();
    private JTextField endTimeField = new JTextField();

    private JTextField priceField = new JTextField();

    private JTextField minimumRatingField = new JTextField();

    private JTextField mentorField = new JTextField();

    private JComponent[] fields = {locationField, capacityField, startDateField, startTimeField, endDateField, endTimeField, priceField, minimumRatingField, mentorField};


    public AddMasterclassDialog(ArrayList<Masterclass> masterclassList) {
        super(false);
        this.masterclassList = masterclassList;
        initChildDialog();
    }

    private void initChildDialog() {
        this.setSize(new Dimension(500, 800));

        addAllFields();
        addButtons();
        this.setVisible(true);
    }

    public AddMasterclassDialog(Masterclass toChange) {
        super(true);

        updatingMasterclass = toChange;

        locationField.setText(toChange.getCity());
        capacityField.setText(Integer.toString(toChange.getCapacity()));
        startDateField.setText(toChange.convertSqlDateToString(toChange.getBeginDate()));
        startTimeField.setText(toChange.getBeginTime().toString());
        endDateField.setText(toChange.convertSqlDateToString(toChange.getEndDate()));
        endTimeField.setText(toChange.getEndTime().toString());
        priceField.setText(Integer.toString(toChange.getPrice()));
        minimumRatingField.setText(Integer.toString(toChange.getMinimumRating()));
        mentorField.setText(Integer.toString(toChange.getMentorId()));
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
                invokeUpdateCallback(newMasterclass);
                JOptionPane.showMessageDialog(this, "De gegevens zijn opgeslagen.");
                this.dispose();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Er is een fout opgetreden");
            }
        } else {
            // Update the masterclass in the database
            try {
                Masterclass updatedMasterclass = fetchUpdatesForMasterclass(updatingMasterclass);
                provider.updateMasterclass(updatedMasterclass);
                invokeUpdateCallback(updatedMasterclass);
                this.dispose();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Er zijn foute gegevens ingevoerd!");
            }
        }
    }

    private Masterclass createNewMasterclass() {
        String location = locationField.getText();
        int capacity = Integer.parseInt(capacityField.getText());
        String startDate = startDateField.getText();
        Time startTime = java.sql.Time.valueOf(startTimeField.getText());
        String endDate = endDateField.getText();
        Time endTime = java.sql.Time.valueOf(endTimeField.getText());
        int price = Integer.parseInt(priceField.getText());
        int minimumRating = Integer.parseInt(minimumRatingField.getText());
        int mentorId = Integer.parseInt(mentorField.getText());

        return new Masterclass(0, location, capacity, startDate, startTime, endDate, endTime, minimumRating, price, null, mentorId);
    }

    private Masterclass fetchUpdatesForMasterclass(Masterclass masterclass) {

        masterclass.setCity(locationField.getText());
        masterclass.setCapacity(Integer.parseInt(capacityField.getText()));
        masterclass.setBeginDate(masterclass.convertStringToSqlDate(startDateField.getText()));

        masterclass.setBeginTime(java.sql.Time.valueOf(startTimeField.getText()));
        masterclass.setEndDate(masterclass.convertStringToSqlDate(endDateField.getText()));
        masterclass.setEndTime(java.sql.Time.valueOf(endTimeField.getText()));
        masterclass.setPrice(Integer.parseInt(priceField.getText()));

        masterclass.setMinimumRating(Integer.parseInt(minimumRatingField.getText()));
        masterclass.setMentorId(Integer.parseInt(mentorField.getText()));

        return masterclass;
    }

    private boolean validateInput() {

        InputType[] masterclassDataTypes = {
                InputType.NAME,     // Locatie
                InputType.CAPACITY, // Capaciteit
                InputType.DATE,     // Start datum
                InputType.TIME,     // Starttijd
                InputType.DATE,     // Eind datun
                InputType.TIME,     // Eindtijd
                InputType.NUMBER,   // Inschrijfgeld / prijs
                InputType.NUMBER,   // Minimale rating
                InputType.NUMBER    // Mentor id
        };

        JTextField[] textFields = new JTextField[]{locationField, capacityField, startDateField, startTimeField, endDateField, endTimeField, priceField, minimumRatingField, mentorField};
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

    private ArrayList<Consumer<Masterclass>> callbackList = new ArrayList<>();

    public void addListener(Consumer<Masterclass> callback) {
        callbackList.add(callback);
    }

    private void invokeUpdateCallback(Masterclass masterclass) {
        for (Consumer<Masterclass> consumer : callbackList) {
            consumer.accept(masterclass);
        }
    }

    @Override
    public void addAllFields() {
        String[] fieldnames = {"Locatie", "Capaciteit", "Startdatum", "Starttijd", "Einddatum", "Eindtijd",
                "Inschrijfgeld",
                "Minimale Rating",
                "Mentor id"};

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
}
