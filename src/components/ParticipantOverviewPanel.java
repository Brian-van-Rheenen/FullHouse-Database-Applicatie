package components;

import backend.PlayerProvider;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ParticipantOverviewPanel extends JPanel {

    private JComboBox <String> toernooiBox = new JComboBox<>();
    private PlayerProvider playerProvider;
    private DefaultTableModel tableModel = new DefaultTableModel();

    public ParticipantOverviewPanel(PlayerProvider playerProvider) {
        this.playerProvider = playerProvider;
    }
}
