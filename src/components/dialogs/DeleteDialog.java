package components.dialogs;

import javax.swing.*;

/**
 * This is the popup / dialog window for removing players.
 */
public class DeleteDialog extends InputDialog {

    /**
     * Create and instantiate the custom dialog window
     */
    public DeleteDialog(int id) {
        final JButton delete = new JButton("Ja");
        delete.addActionListener(e -> {
            JOptionPane pane = getOptionPane((JComponent)e.getSource());
            pane.setValue(delete);
        });

        final JButton cancel = new JButton("Nee");
        cancel.addActionListener(e -> {
            JOptionPane pane = getOptionPane((JComponent)e.getSource());
            pane.setValue(cancel);
        });

        Object[] inputFields = {"Weet u zeker dat u de persoon met id: " + id + " wilt verwijderen?",
                                "Dit kan NIET ongedaan gemaakt worden."};

        int option = JOptionPane.showOptionDialog(this, inputFields, "Speler verwijderen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{delete, cancel}, delete);

        if (option == JOptionPane.OK_OPTION) {
            // verwijderen
        }
    }
}
