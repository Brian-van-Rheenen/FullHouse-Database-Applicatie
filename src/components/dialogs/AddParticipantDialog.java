package components.dialogs;


import models.Deelname;
import models.Event;
import models.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

public class AddParticipantDialog extends BasicDialog {

    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();

    private JTextField toernooiCodeField = new JTextField();
    private JTextField playerNameField = new JTextField();
    private JTextField postcode = new JTextField();
    private JLabel[] fieldLabels = {new JLabel("Event code"), new JLabel("Naam speler"), new JLabel("Postcode")};

    public AddParticipantDialog(boolean isForChange) {
        super(isForChange);
        addAllFields();
        addButtons();
        this.setSize(400,350);
        this.setVisible(true);
    }

    @Override
    public void addAllFields() {

        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Voeg een deelname toe");
        title.setFont(new Font("Helvetica", Font.BOLD, 30));
        panel.add(title);
        this.add(panel);
        this.add(Box.createRigidArea(new Dimension(300, 20)));

        for (int i = 0; i < 2; i++) {
            JPanel labelPanel = new JPanel();
            labelPanel.setLayout(new BorderLayout());
            JLabel fieldLabel = fieldLabels[i];

            fieldLabel.setFont(new Font("Helvetica", Font.BOLD, 15));

            labelPanel.add(fieldLabel);
            this.add(labelPanel);
            JTextField textField = getAllTextFields()[i];
            textField.setFont(new Font("Helvetica", Font.PLAIN, 20));
            textField.setPreferredSize(new Dimension(200,30));
            this.add(textField);
        }
        this.add(Box.createRigidArea(new Dimension(300, 20)));

    }


    private JTextField[] getAllTextFields() {
        return new JTextField[]{toernooiCodeField, playerNameField, postcode};
    }

    @Override
    public void handleConfirm() {
        String inputForEvent = toernooiCodeField.getText();
        String inputForPLayer = playerNameField.getText();
        String inputForZIP = postcode.getText();

        Optional<Event> optionalEvent = events.stream().filter(toernooi -> toernooi.isMatchForSearch(inputForEvent)).findAny();
        Optional<Player> optionalPlayer = players.stream()
                .filter(player -> player.getName()
                        .equalsIgnoreCase(inputForPLayer)
                        && player.getAddress().getZipCode().equalsIgnoreCase(inputForZIP)).findAny();

        if (!optionalEvent.isPresent()) {
            JOptionPane.showMessageDialog(this, "Het systeem kon de masterclass/toernooi niet vinden");
        } else if (!optionalPlayer.isPresent()) {
            JOptionPane.showMessageDialog(this, "Het systeem kon geen speler vinden met deze postcode");

        } else {
            Player player = optionalPlayer.get();

            optionalEvent.get().getParticipants().add(new Deelname(player, false));
        }

    }
}