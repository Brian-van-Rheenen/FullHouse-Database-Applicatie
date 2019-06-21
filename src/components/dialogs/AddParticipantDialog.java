package components.dialogs;


import models.Participant;
import models.Event;
import models.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

public class AddParticipantDialog extends BasicDialog {



    private JTextField tournamentCodeField = new JTextField();
    private JTextField playerCodeField = new JTextField();

    private JComponent [] fields={tournamentCodeField, playerCodeField};

    private String[] fieldLabels = {"Event code", "Code speler"};

    public AddParticipantDialog(boolean isForChange) {
        super(isForChange);
        this.setTitle("Voeg een deelname toe");
        addAllFields();
        addButtons();
        this.setSize(400,200);
        this.setVisible(true);
    }

    public String getInputForEvent(){
        return tournamentCodeField.getText();
    }

    public String getInputForPlayer(){
        return playerCodeField.getText();
    }



    public void addAllFields() {

       super.addAllFields(fields, fieldLabels);

    }


    private JTextField[] getAllTextFields() {
        return new JTextField[]{tournamentCodeField, playerCodeField};
    }

    @Override
    public void handleConfirm() {

        this.dispose();
    }


}