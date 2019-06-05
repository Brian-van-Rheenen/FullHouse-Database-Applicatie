package components;

import components.dialogs.AddInputDialog;
import models.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

/**
 * This is the general overview panel for users
 */
public class UserOverviewPanel extends JPanel {

    public UserOverviewPanel() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new EtchedBorder()));

        JPanel leftMenuPanel = new JPanel(new GridBagLayout());
        leftMenuPanel.setBackground(Color.GREEN);

        Vector<String> collumnNames = new Vector<>();
        collumnNames.add("name");
        collumnNames.add("sex");

        // Date of Birth
        collumnNames.add("DoB");

        // TODO: Replace with data from the database
        Vector<Player> players = new Vector<>();

        DefaultTableModel model = new DefaultTableModel(players, collumnNames);
        addLeftMenuButtons(leftMenuPanel);

        TablePanel tablePanel = new TablePanel(model);
        this.add(tablePanel, BorderLayout.CENTER);
    }

    private void addLeftMenuButtons(JPanel leftMenuPanel) {
        JPanel leftMenuButtonPanel = getLeftMenuButtonsPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weighty = 1.0;

        leftMenuPanel.add(leftMenuButtonPanel, gbc);

        this.add(leftMenuPanel, BorderLayout.LINE_START);
    }

    private JPanel getLeftMenuButtonsPanel() {
        JPanel leftMenuButtonPanel = new JPanel(new GridLayout(3,1, 20, 20));
        leftMenuButtonPanel.setPreferredSize(new Dimension(150, 200));
        JButton addButton = new JButton("Toevoegen");
        addButton.setPreferredSize(new Dimension(150, 200));
        addButton.addActionListener(e -> {
            new AddInputDialog();
        });

        JButton editButton = new JButton("Wijzigen");
        editButton.setPreferredSize(new Dimension(150, 200));

        JButton deleteButton = new JButton("Verwijderen");
        deleteButton.setPreferredSize(new Dimension(150, 200));

        leftMenuButtonPanel.add(addButton);
        leftMenuButtonPanel.add(editButton);
        leftMenuButtonPanel.add(deleteButton);
        return leftMenuButtonPanel;
    }
}
