package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class TabbedPanel extends JPanel {

    public TabbedPanel() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new EtchedBorder()));

        JPanel leftMenuPanel = new JPanel(new GridBagLayout());
        leftMenuPanel.setBackground(Color.GREEN);

        JPanel cardPanel = new JPanel(new CardLayout());
        cardPanel.setBackground(Color.CYAN);

        JPanel leftMenuButtonPanel = new JPanel(new GridLayout(3,1, 20, 20));
        leftMenuButtonPanel.setPreferredSize(new Dimension(150, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weighty = 1.0;

        JButton addButton = new JButton("Toevoegen");
        addButton.setPreferredSize(new Dimension(150, 200));

        JButton editButton = new JButton("Wijzigen");
        editButton.setPreferredSize(new Dimension(150, 200));

        JButton deleteButton = new JButton("Verwijderen");
        deleteButton.setPreferredSize(new Dimension(150, 200));

        leftMenuButtonPanel.add(addButton);
        leftMenuButtonPanel.add(editButton);
        leftMenuButtonPanel.add(deleteButton);

        leftMenuPanel.add(leftMenuButtonPanel, gbc);

        this.add(leftMenuPanel, BorderLayout.LINE_START);
        this.add(cardPanel, BorderLayout.CENTER);
    }
}
