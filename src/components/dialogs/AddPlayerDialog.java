package components.dialogs;

import backend.PlayerProvider;
import models.Player;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Consumer;

public class AddPlayerDialog extends BasicDialog {

    private ArrayList<Player> playerList;
    private PlayerProvider provider = new PlayerProvider();

    private JTextField nameField = new JTextField();

    //adres
    private JTextField streetField = new JTextField();
    private JTextField houseNrField = new JTextField();

    private JTextField postcodeField = new JTextField();
    private JTextField cityField = new JTextField();

    //------
    private JComboBox<String> genderBox = new JComboBox<>();

    private JTextField dob = new JTextField();

    private JTextField telephoneNR = new JTextField();
    private JTextField emailTextField = new JTextField();

    private JComponent[] fields = {nameField, streetField, houseNrField, postcodeField, cityField, genderBox, dob, telephoneNR, emailTextField};


    public AddPlayerDialog(ArrayList<Player> playerList) {
        super(false);
        this.playerList = playerList;
        initChildDialog();
    }

    private void initChildDialog() {
        this.setSize(new Dimension(500, 800));

        addAllFields();
        addButtons();
        this.setVisible(true);
    }

    public AddPlayerDialog(Player toChange) {
        super(true);

        telephoneNR.setText(toChange.getTelephoneNR());
        // TODO: check where to get streetfield
        streetField.setText("");
        nameField.setText(toChange.getName());
        // TODO: check house nr
        houseNrField.setText("");
        postcodeField.setText(toChange.getZip());
        genderBox.setSelectedItem(toChange.getGender());
        emailTextField.setText(toChange.getEmail());
        this.dob.setText(toChange.getDob().toString());
        this.cityField.setText(toChange.getCity());
        initChildDialog();
    }

    @Override
    public void handleConfirm() {
        if (!checkAllFields()) {
            JOptionPane.showMessageDialog(this, "Iets ging niet helemaal goed");
        } else {
            if (!this.isForChange()) {

                try {
                    Player newPlayer = createNewPlayer();

                    // Attempt to add to the database
                    provider.addPlayer(newPlayer);
                    this.playerList.add(newPlayer);
                    invokeUpdateCallback();
                    JOptionPane.showMessageDialog(this, "De gegevens zijn opgeslagen.");
                    this.dispose();
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Er is een fout opgetreden");
                }
            }
        }
    }

    private Player createNewPlayer() {
        String telephone = telephoneNR.getText();
        String street = streetField.getText();
        String name = nameField.getText();
        int houseNr = Integer.parseInt(houseNrField.getText());
        String zip = postcodeField.getText();
        String gender = (String) genderBox.getSelectedItem();
        String email = emailTextField.getText();

        Date dateOfBirth = new Date();

        try {
            dateOfBirth = new SimpleDateFormat("dd-MM-yyyy").parse(dob.getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String city = this.cityField.getText();
        return new Player(0, name, gender, dateOfBirth, street, houseNr, zip, city, telephone, email, 0);
    }

    private boolean checkAllFields() {

        InputType[] playerDataTypes = {
                InputType.NAME,         // Naam
                InputType.NAME,         // Straat
                InputType.NUMBER,       // Huisnummer
                InputType.POST_CODE,    // Postcode
                InputType.CITY,         // Woonplaats
                                        // Geslacht
                InputType.DATE,         // GeboorteDatum
                InputType.TELEPHONE_NR, // Telefoonnummer
                InputType.EMAIL         // Email
        };

        JTextField[] textFields = new JTextField[]{nameField, streetField, houseNrField, postcodeField, cityField, dob, telephoneNR, emailTextField};
        boolean res = true;

        for (int i = 0; i < playerDataTypes.length; i++) {
            JTextField textField = textFields[i];
            String input = textField.getText();
            boolean goodInput = playerDataTypes[i].isGoodInput(input);

            if (goodInput) {
                unmark(textField);

            } else {
                mark(textField);
                res = false;
            }
        }

        return res;
    }

    private ArrayList<Consumer<Void>> callbackList = new ArrayList<>();

    public void addListener(Consumer<Void> callback) {
        callbackList.add(callback);
    }

    private void invokeUpdateCallback() {
        for (Consumer<Void> consumer : callbackList) {
            consumer.accept(null);
        }
    }

    @Override
    public void addAllFields() {
        initComboBox();

        String[] fieldnames = {"Naam", "Straat", "Huisnummer", "Postcode", "Woonplaats", "Geslacht",
                "Geboortedatum",
                "Telefoonnummer",
                "Emailadres"};

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

    private void initComboBox() {
        String[] genders = {"M", "V", "O"};
        Arrays.stream(genders).forEach(g -> genderBox.addItem(g));
        genderBox.setSelectedIndex(2);
    }
}
