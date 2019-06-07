package components.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class OverviewPanel extends JPanel {

    private JPanel leftMenuButtonPanel = new JPanel(new GridLayout(3, 1, 20, 20));

    public OverviewPanel() {
        super();

        JPanel leftMenuPanel = new JPanel(new GridBagLayout());
        leftMenuPanel.setBackground(Color.LIGHT_GRAY);

        leftMenuButtonPanel.setPreferredSize(new Dimension(150, 200));
        leftMenuButtonPanel.setBackground(Color.LIGHT_GRAY);

        setLookAndFeel();

        addLeftMenuButtons(leftMenuPanel);
    }

    protected abstract void createButtons();

    private void setLookAndFeel() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private void addLeftMenuButtons(JPanel leftMenuPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weighty = 1.0;

        leftMenuPanel.add(leftMenuButtonPanel, gbc);

        this.add(leftMenuPanel, BorderLayout.LINE_START);
    }

    public void addButtonToPanel(JButton button) {
        leftMenuButtonPanel.add(button);
    }
}
