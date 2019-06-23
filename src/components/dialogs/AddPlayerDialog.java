package components.dialogs;

import backend.PlayerProvider;
import components.dialogs.exceptions.ExceptionDialog;
import models.Address;
import models.Gender;
import models.Player;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

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
    private JComboBox<Gender> genderBox = new JComboBox<>(Gender.values());

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
        nameField.setText(toChange.getName());

        genderBox.setSelectedIndex(0);
        DefaultComboBoxModel<Gender> model = (DefaultComboBoxModel<Gender>) genderBox.getModel();
        model.setSelectedItem(toChange.getGender());

        emailTextField.setText(toChange.getEmail());
        dob.setText(toChange.convertSqlDateToString(toChange.getDob()));

        cityField.setText(toChange.getAddress().getCity());
        streetField.setText(toChange.getAddress().getStreet());
        houseNrField.setText(Integer.toString(toChange.getAddress().getHouseNr()));
        zipCodeField.setText(toChange.getAddress().getZipCode());

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
                JOptionPane.showMessageDialog(this, "De gegevens zijn opgeslagen.");
                this.dispose();
            } catch (SQLException e) {
                new ExceptionDialog("Er is een fout opgetreden en het is niet gelukt om de speler toe te voegen.");
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

                // Close the screen
                this.dispose();
            } catch (SQLException e) {
                new ExceptionDialog("Er is een fout opgetreden en het is niet gelukt om de speler aan te passen.");
            }
        }
    }

    private Player createNewPlayer() {

        String telephone = telephoneNR.getText();
        String street = streetField.getText();
        String name = nameField.getText();
        int houseNr = Integer.parseInt(houseNrField.getText());
        String zip = zipCodeField.getText();
        Gender gender = (Gender) genderBox.getSelectedItem();
        String email = emailTextField.getText();

        String city = this.cityField.getText();
        return new Player(new Address(city, street, houseNr, zip), name, gender, convertStringToJavaDate(dob.getText()), telephone, email, 0);
    }

    private Player fetchUpdatesForPlayer(Player player) {
        player.setName(nameField.getText());
        player.setGender((Gender) genderBox.getSelectedItem());
        player.setDob(Player.convertJavaDateToSqlDate(convertStringToJavaDate(dob.getText())));

        player.getAddress().setCity(cityField.getText());
        player.getAddress().setStreet(streetField.getText());
        player.getAddress().setHouseNr(Integer.parseInt(houseNrField.getText()));
        player.getAddress().setZipCode(zipCodeField.getText());

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

        LocalDate dateOfBirth = convertStringToJavaDate(dob.getText()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();

        if (age < 18) return false;

        return super.validateInput(playerDataTypes, textFields);
    }

    public void addAllFields() {
        String[] fieldnames = {"Naam", "Straat", "Huisnummer", "Postcode", "Woonplaats", "Geslacht",
                "Geboortedatum",
                "Telefoonnummer",
                "Emailadres"};

        super.addAllFields(fields, fieldnames);
    }

    public Date convertStringToJavaDate(String date) {
        Date dateOfBirth = new Date();

        try {
            dateOfBirth = new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            new ExceptionDialog("Het is niet gelukt om het volgende te parsen: " + date);
        }

        return dateOfBirth;
    }
}
