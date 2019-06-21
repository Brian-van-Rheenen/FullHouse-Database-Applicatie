package components.dialogs;


import models.Participant;
import models.Event;
import models.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

public class AddParticipantDialog extends BasicDialog {

    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();

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

    public void addAllFields() {

       super.addAllFields(fields, fieldLabels);

    }


    private JTextField[] getAllTextFields() {
        return new JTextField[]{tournamentCodeField, playerCodeField};
    }

    @Override
    public void handleConfirm() {
        String inputForEvent = tournamentCodeField.getText();
        String inputForPLayer = playerCodeField.getText();


        Optional<Event> optionalEvent = events
                .stream()
                .filter(toernooi -> toernooi.isMatchForSearch(inputForEvent)).findAny();

        Optional<Player> optionalPlayer = players
                .stream()
                .filter(player -> Integer.toString(player.getId()).equals(inputForPLayer)).findAny();

        if (!optionalEvent.isPresent()) {
            JOptionPane.showMessageDialog(this, "Het systeem kon de masterclass/toernooi niet vinden");
        } else if (!optionalPlayer.isPresent()) {
            JOptionPane.showMessageDialog(this, "Het systeem kon geen speler vinden met deze code");

        } else {
            Player player = optionalPlayer.get();

            optionalEvent.get().getParticipants().add(new Participant(player, false));
        }

    }


}