package components.dialogs;

import models.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

public class TestPlayerDialog extends JDialog {

    private ArrayList<Player> playerlist;

    private JTextField nameField = new JTextField();

    //adres
    private JTextField streetField = new JTextField();
    private JTextField houseNrField = new JTextField();
    private JTextField toevoegingen = new JTextField();

    private JTextField postcodeField = new JTextField();
    private JTextField cityField = new JTextField();

    //------
    private JComboBox<String> genderBox = new JComboBox<>();

    private JTextField dob = new JTextField();

    private JTextField telephoneNR = new JTextField();
    private JTextField emailTextField = new JTextField();

    private JComponent[] fields = {nameField, streetField, houseNrField, postcodeField, cityField, genderBox, dob, telephoneNR, emailTextField};


    public TestPlayerDialog(ArrayList<Player> playerlist) {
        this.playerlist = playerlist;
        setLayoutAndBorder();
        this.setResizable(false);

        this.setSize(new Dimension(500, 800));

        initCombobox();
        addAllFields();
        addButtons();
        this.setVisible(true);

    }

    private void addPlayerToList(){
        String telephone = telephoneNR.getText();
        String street = streetField.getText();
        String name = nameField.getText();
        int houseNr = Integer.parseInt(houseNrField.getText());
        String zip = postcodeField.getText();
        String gender = (String) genderBox.getSelectedItem();
        String email = emailTextField.getText();
        String data = this.dob.getText();
        String city = this.cityField.getText();

        this.playerlist.add(new Player(name, street, houseNr, zip, city, gender, telephone, email, data));
    }

    private boolean checkAllFields() {

        InputType[] playerDatatypes = InputType.getPersonalDataTypes();
        JTextField[] textFields = this.getAllTextFields();
        boolean res = true;

        for (int i = 0; i < playerDatatypes.length; i++) {
            JTextField textField = textFields[i];
            String input = textField.toString();
            boolean isMarked = playerDatatypes[i].isGoodInput(input);
            if (isMarked) {
                mark(textField);
                res = false;
            } else {
                unmark(textField);
            }
        }


        return res;
    }

    private void setLayoutAndBorder() {
        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);
        BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
        EmptyBorder emptyBorder = new EmptyBorder(10, 5, 10, 5);
        contentPane.setBorder(emptyBorder);


        this.getContentPane().setLayout(boxLayout);
    }


    private void addAllFields() {

        String[] fieldnames = {"Naam", "Straat", "Huisnummer", "Postcode", "Woonplaats", "Geslacht",
                "Geboortedatum",
                "Telefoonnummer",
                "Emailadres"};

        int nrOfFields = fields.length;

        for (int i = 0; i < nrOfFields; i++) {

            Label label = new Label(fieldnames[i]);
            label.setFont(new Font("Helvetica", Font.BOLD, 12));

            this.add(label);

            JComponent field = fields[i];
            field.setFont(new Font("Helvetica", Font.PLAIN, 20));
            this.add(field);
            this.add(Box.createRigidArea(new Dimension(300, 9)));
        }
    }

    private void addButtons() {
        JButton submit = new JButton("Toevoegen");
        JButton cancel = new JButton("Annuleer");

        Font buttonFont = new Font("Helvetica", Font.PLAIN, 20);

        submit.setFont(buttonFont);
        cancel.setFont(buttonFont);

        submit.addActionListener(event -> {
            if (!checkAllFields()) {
                JOptionPane.showMessageDialog(this, "Iets ging niet helemaal goed");
            } else {
                JOptionPane.showMessageDialog(this, "Alles ging goed");
                addPlayerToList();
            }
        });


        cancel.addActionListener(e -> this.dispose());

        JPanel buttonPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(1, 2);
        gridLayout.setVgap(10);
        gridLayout.setHgap(10);

        buttonPanel.setLayout(gridLayout);
        buttonPanel.add(submit);
        buttonPanel.add(cancel);

        this.getContentPane().add(buttonPanel);


    }


    private void initCombobox() {
        String[] genders = {"Man", "Vrouw", "Overig"};
        Arrays.stream(genders).forEach(g -> genderBox.addItem(g));
        genderBox.setSelectedIndex(2);


    }


    private JTextField[] getAllTextFields() {
        JTextField[] res = {nameField, streetField, houseNrField, postcodeField, cityField, dob, telephoneNR, emailTextField};
        return res;
    }

    private void unmark(JTextField toUnmark) {
        toUnmark.setForeground(Color.BLACK);

        toUnmark.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    private void mark(JTextField toMark) {

        toMark.setForeground(Color.RED);
        toMark.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
    }


}
