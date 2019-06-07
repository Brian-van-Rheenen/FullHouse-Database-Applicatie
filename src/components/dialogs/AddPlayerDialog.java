package components.dialogs;

import models.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AddPlayerDialog extends BasicDialog {

    private ArrayList<Player> playerList;

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


        streetField.setText(toChange.getStreet());
        nameField.setText(toChange.getName());
        houseNrField.setText(String.valueOf(toChange.getHouseNr()));
        postcodeField.setText(toChange.getZip());
        genderBox.setSelectedItem(toChange.getGender());
        emailTextField.setText(toChange.getEmail());
        this.dob.setText(toChange.getDobString());
        this.cityField.setText(toChange.getWoonplaats());
        initChildDialog();
    }


    private void addPlayerToList() {
        this.playerList.add(createNewPlayer());
    }

    @Override
    public void handleConfirm() {
        if (!checkAllFields()) {
            JOptionPane.showMessageDialog(this, "Iets ging niet helemaal goed");
        } else {
            if (!this.isForChange()) {
                addPlayerToList();
            }

            // TODO: Update player
            JOptionPane.showMessageDialog(this, "De gegevens zijn opgeslagen.");
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
        String data = this.dob.getText();
        String city = this.cityField.getText();
        return new Player(name, 0, street, houseNr, zip, city, data, email, telephone, gender);
    }

    private boolean checkAllFields() {

        InputType[] playerDatatypes = InputType.getPersonalDataTypes();
        JTextField[] textFields = this.getAllTextFields();
        boolean res = true;

        for (int i = 0; i < playerDatatypes.length; i++) {
            JTextField textField = textFields[i];
            String input = textField.getText();
            boolean goodInput = playerDatatypes[i].isGoodInput(input);


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
        initCombobox();

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


    private void initCombobox() {
        String[] genders = {"M", "V", "O"};
        Arrays.stream(genders).forEach(g -> genderBox.addItem(g));
        genderBox.setSelectedIndex(2);
    }


    private JTextField[] getAllTextFields() {
        JTextField[] res = {nameField, streetField, houseNrField, postcodeField, cityField, dob, telephoneNR, emailTextField};
        return res;
    }
}