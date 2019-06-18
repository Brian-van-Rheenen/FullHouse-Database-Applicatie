package components.dialogs;

import javax.swing.*;

/**
 * This is the popup / dialog window for notifying the user that no player is selected.
 */
public class NoSelectionDialog extends InputDialog {

    /**
     * Create and instantiate the custom dialog window
     */
    public NoSelectionDialog(String subject) {
        final JButton ok = new JButton("OK");
        ok.addActionListener(e -> {
            JOptionPane pane = getOptionPane((JComponent)e.getSource());
            pane.setValue(ok);
        });

        Object[] inputFields = {"Foutmelding: er is geen " + subject + " geselecteerd."};

        JOptionPane.showOptionDialog(this, inputFields, "Geen " + subject + " geselecteerd", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{ok}, ok);
    }
}
