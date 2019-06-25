package components.dialogs;

import javax.swing.*;

/**
 * This is the popup / dialog window for removing players.
 * The database and model are automatically updated on confirmation
 */
public class DeleteDialog extends InputDialog {

    private int id;

    /**
     * Create and instantiate the custom dialog window
     */
    public DeleteDialog(int id) {
        this.id = id;
    }

    /**
     * Show the dialog and get the results back
     * @return Confirmation or cancel of the dialog
     */
    public int showDialog() {

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

        Object[] inputFields = {"Weet u zeker dat u de persoongegevens van de persoon met id: " + id + " wilt verwijderen?",
                "Dit kan NIET ongedaan gemaakt worden."};

        return JOptionPane.showOptionDialog(
                this,
                inputFields,
                "Speler verwijderen",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{delete, cancel},
                delete
        );
    }
}
