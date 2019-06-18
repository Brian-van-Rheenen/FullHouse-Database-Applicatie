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
import java.util.List;
import java.util.function.Consumer;

public class AddPlayerDialog extends BasicDialog {

    private List<Player> playerList;
    private PlayerProvider provider = new PlayerProvider();
    private Player updatingPlayer = null;

    private JTextField nameField = new JTextField();

    //adres
    private JTextField streetField = new JTextField();
    private JTextField houseNrField = new JTextField();

    private JTextField zipCodeField = new JTextField();
    private JTextField cityField = new JTextField();

    //------
    private JComboBox<String> genderBox = new JComboBox<>();

    private JTextField dob = new JTextField();

    private JTextField telephoneNR = new JTextField();
    private JTextField emailTextField = new JTextField();

    private JComponent[] fields = {nameField, streetField, houseNrField, zipCodeField, cityField, genderBox, dob, telephoneNR, emailTextField};


    public AddPlayerDialog(List<Player> players) {
        super(false);
        playerList = players;
        initChildDialog();
    }

    private void initChildDialog() {
        this.setSize(new Dimension(500, 800));

        addAllFields();
        addButtons();
        this.setVisible(true);
    }

    public AddPlayerDialog(List<Player> players, Player toChange) {
        super(true);

        playerList = players;
        updatingPlayer = toChange;

        telephoneNR.setText(toChange.getTelephoneNR());
        streetField.setText(toChange.getStreet());
        nameField.setText(toChange.getName());
        houseNrField.setText(Integer.toString(toChange.getHouseNr()));
        zipCodeField.setText(toChange.getZip());
        genderBox.setSelectedItem(toChange.getGender());
        emailTextField.setText(toChange.getEmail());
        dob.setText(toChange.convertSqlDateToString(toChange.getDob()));
        this.cityField.setText(toChange.getCity());
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
            // Create

            try {
                // Attempt to add to the database, get the updated player with id back
                Player newPlayer = provider.addPlayer(createNewPlayer());

                this.playerList.add(newPlayer);
//                invokeUpdateCallback(newPlayer);
                JOptionPane.showMessageDialog(this, "De gegevens zijn opgeslagen.");
                this.dispose();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Er is een fout opgetreden");
            }
        } else {
            // Update the player in the database
            try {
                // Fetch updates from the screen
                Player updatedPlayer = fetchUpdatesForPlayer(updatingPlayer);
                provider.updatePlayer(updatedPlayer);

                // Update the model
                int index = playerList.indexOf(updatedPlayer);
                if(index == -1) {
                    // Could not find the model, something went wrong
                    // Recover by refreshing the entire list
                    playerList.clear();
                    playerList.addAll(provider.allPlayers());
                } else {
                    // Only update the player in the list
                    playerList.set(index, updatedPlayer);
                }

//                invokeUpdateCallback(updatedPlayer);
                // Close the screen
                this.dispose();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Er zijn foute gegevens ingevoerd!");
            }
        }
    }

    private Player createNewPlayer() {

        String telephone = telephoneNR.getText();
        String street = streetField.getText();
        String name = nameField.getText();
        int houseNr = Integer.parseInt(houseNrField.getText());
        String zip = zipCodeField.getText();
        String gender = (String) genderBox.getSelectedItem();
        String email = emailTextField.getText();

        Date dateOfBirth = new Date();

        try {
            dateOfBirth = new SimpleDateFormat("dd-MM-yyyy").parse(dob.getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String city = this.cityField.getText();
        return new Player(0, 0, name, gender, dateOfBirth, street, houseNr, zip, city, telephone, email, 0);
    }

    private Player fetchUpdatesForPlayer(Player player) {

        Date dateOfBirth = new Date();

        try {
            dateOfBirth = new SimpleDateFormat("dd-MM-yyyy").parse(dob.getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        player.setName(nameField.getText());
        player.setGender((String) genderBox.getSelectedItem());
        player.setDob(Player.convertJavaDateToSqlDate(dateOfBirth));

        player.setCity(cityField.getText());
        player.setStreet(streetField.getText());
        player.setHouseNr(Integer.parseInt(houseNrField.getText()));
        player.setZip(zipCodeField.getText());

        player.setEmail(emailTextField.getText());
        player.setTelephoneNR(telephoneNR.getText());

        return player;
    }

    /**
     * Validate the input on the form
     * @return true if the input is valid
     */
    private boolean validateInput() {

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

        JTextField[] textFields = new JTextField[]{nameField, streetField, houseNrField, zipCodeField, cityField, dob, telephoneNR, emailTextField};
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

    private ArrayList<Consumer<Player>> callbackList = new ArrayList<>();

    public void addListener(Consumer<Player> callback) {
        callbackList.add(callback);
    }

    private void invokeUpdateCallback(Player player) {
        for (Consumer<Player> consumer : callbackList) {
            consumer.accept(player);
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
