package components.dialogs;

import backend.PlayerProvider;
import models.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the popup / dialog window for adding players.
 */
public class AddInputDialog extends InputDialog {
    PlayerProvider playerProvider = new PlayerProvider();

    /**
     * Create and instantiate the custom dialog window
     */
    public AddInputDialog() {
        JTextArea errors = new JTextArea();
        errors.setMinimumSize(new Dimension(250, 50));
        errors.setPreferredSize(new Dimension(250, 50));
        errors.setEditable(false);
        errors.setLineWrap(true);
        errors.setWrapStyleWord(true);
        errors.setOpaque(false);
        errors.setForeground(Color.red);

        final JButton add = new JButton("Toevoegen");
        add.addActionListener(e -> {
            JOptionPane pane = getOptionPane((JComponent)e.getSource());
            pane.setValue(add);
        });
        add.setEnabled(false);

        final JButton cancel = new JButton("Annuleren");
        cancel.addActionListener(e -> {
            JOptionPane pane = getOptionPane((JComponent)e.getSource());
            pane.setValue(cancel);
        });

        JTextField nameField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField streetField = new JTextField();
        JTextField houseNrField = new JTextField();
        JTextField postalcodeField = new JTextField();

        String[] genders = { "Man", "Vrouw", "Onbekend" };
        JComboBox genderList = new JComboBox(genders);
        genderList.setSelectedIndex(0);

        JTextField dateOfBirthField = new JTextField();

        dateOfBirthField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                formatter.setLenient(false);

                JTextField dateOfBirthField = (JTextField)e.getSource();

                if(dateOfBirthField.getText().matches("([0-9]{2})-([0-9]{2})-([0-9]{4})")) {
                    try {
                        formatter.parse(dateOfBirthField.getText());
                        errors.setText("");
                        add.setEnabled(true);
                        return;
                    } catch (ParseException pe) {
                        add.setEnabled(false);
                        errors.setText("Geboortedatum is geen correcte datum.");
                        return;
                    }
                }
                else {
                    add.setEnabled(false);
                    errors.setText("Geboortedatum is niet in het correcte formaat (dd-MM-yyyy). Voorbeeld: 03-06-2000.");
                    return;
                }
            }
        });

        JTextField phoneNrField = new JTextField();
        JTextField emailField = new JTextField();

        Object[] inputFields = {"Naam", nameField,
                                "Woonplaats", cityField,
                                "Straat", streetField,
                                "Huisnummer", houseNrField,
                                "Postcode", postalcodeField,
                                "Geslacht", genderList,
                                "Geboortedatum", dateOfBirthField,
                                "Telefoonnummer", phoneNrField,
                                "Emailadres", emailField,
                                "", errors};

        int option = JOptionPane.showOptionDialog(this, inputFields, "Speler toevoegen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{add, cancel}, add);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String city = cityField.getText();
            String street = streetField.getText();
            int houseNr = Integer.parseInt(houseNrField.getText());
            String postalcode = postalcodeField.getText();
            String gender = new String();
            switch (genderList.getSelectedItem().toString()) {
                case "Man":
                    gender = "M";
                    break;
                case "Vrouw":
                    gender = "V";
                    break;
                case "Onbekend":
                    gender = "O";
                    break;
            }

            Date dateOfBirth = new Date();

            try {
                dateOfBirth = new SimpleDateFormat("dd-MM-yyyy").parse(dateOfBirthField.getText());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String phoneNr = phoneNrField.getText();
            String email = emailField.getText();

            Player player = new Player(0, name, gender, dateOfBirth, street, houseNr, postalcode, city, phoneNr, email, 0);

            try {
                playerProvider.addPlayer(player);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Er is iets fout gegaan met het toevoegen van de speler. Probeer het opnieuw.", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
