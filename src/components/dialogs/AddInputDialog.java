package components.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * This is the popup / dialog window for adding players.
 */
public class AddInputDialog extends Component {

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

        String[] genders = { "Man", "Vrouw" };
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
            // toevoegen
        }
    }

    /**
     * Find the parent JOptionPane. This keeps looping until it finds the parent
     * @param parent the component of which you need to know the parent
     * @return the parent JOptionPane
     */
    protected JOptionPane getOptionPane(JComponent parent) {
        JOptionPane pane;
        if (!(parent instanceof JOptionPane)) {
            pane = getOptionPane((JComponent)parent.getParent());
        } else {
            pane = (JOptionPane) parent;
        }
        return pane;
    }
}
