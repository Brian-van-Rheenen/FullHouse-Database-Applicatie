package components.dialogs;

import backend.MentorProvider;
import models.Mentor;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class AddMentorDialog extends BasicDialog {

    private List<Mentor> mentors;
    private MentorProvider provider = new MentorProvider();
    private Mentor updatingMentor;

    // region Fields

    private JTextField nameField        = new JTextField();
    private JTextField phoneNumberField = new JTextField();
    private JTextField emailField       = new JTextField();

    private JTextField[] fields = {
            nameField,
            phoneNumberField,
            emailField
    };

    // endregion Fields

    public AddMentorDialog(List<Mentor> mentors) {
        super(false);

        this.mentors = mentors;
        initChildDialog();
    }

    public AddMentorDialog(List<Mentor> mentors, Mentor toUpdate) {
        super(true);

        this.mentors = mentors;
        this.updatingMentor = toUpdate;

        nameField.setText(updatingMentor.getName());
        phoneNumberField.setText(updatingMentor.getPhoneNumber());
        emailField.setText(updatingMentor.getEmail());

        initChildDialog();
    }

    private void initChildDialog() {
        this.setSize(500, 800);
        this.setTitle("Voeg een Mentor toe");
        this.addAllFields();
        this.addButtons();
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void handleConfirm() {
        if(!validateInput()) {
            JOptionPane.showMessageDialog(this, "Er zijn foute gegevens ingevoerd!");
        } else {

            if(!isForChange()) {
                // Create

                try {
                    // Attempt to add to the database, get the updated mentor with id back
                    Mentor newMentor = provider.addMentor(createNewMentor());

                    this.mentors.add(newMentor);
                    JOptionPane.showMessageDialog(this, "De gegevens zijn opgeslagen.");
                    this.dispose();
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Er is een fout opgetreden");
                }
            } else {
                // Update
                try {
                    // Fetch updates from the screen
                    Mentor updatedMentor = fetchUpdatesForMentor(updatingMentor);
                    provider.updateMentor(updatedMentor);

                    // Update the model
                    int index = mentors.indexOf(updatedMentor);
                    if(index == -1) {
                        // Could not find the model, something went wrong
                        // Recover by refreshing the entire list
                        mentors.clear();
                        mentors.addAll(provider.getAllMentors());
                    } else {
                        // Only update the player in the list
                        mentors.set(index, updatedMentor);
                    }

                    // Close the screen
                    this.dispose();
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Er zijn foute gegevens ingevoerd!");
                }
            }

        }
    }

    private Mentor fetchUpdatesForMentor(Mentor updatingMentor) {

        updatingMentor.setName(nameField.getText());
        updatingMentor.setPhoneNumber(phoneNumberField.getText());
        updatingMentor.setEmail(emailField.getText());

        return updatingMentor;
    }

    private Mentor createNewMentor() {
        String name        = nameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email       = emailField.getText();

        return new Mentor(name, phoneNumber, email);
    }

    private boolean validateInput() {
        InputType[] inputTypes = {
                InputType.NAME,
                InputType.TELEPHONE_NR,
                InputType.EMAIL
        };

        return super.validateInput(inputTypes, fields);
    }

    private void addAllFields() {
        String[] names = {
                "Naam",
                "Telefoonnummer",
                "Email"
        };

        super.addAllFields(fields, names);
    }
}
