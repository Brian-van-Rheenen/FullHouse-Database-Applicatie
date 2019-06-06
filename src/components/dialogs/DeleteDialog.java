package components.dialogs;

import backend.PlayerProvider;

import javax.swing.*;
import java.sql.SQLException;

/**
 * This is the popup / dialog window for removing players.
 */
public class DeleteDialog extends InputDialog {
    private PlayerProvider playerProvider = new PlayerProvider();

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

        Object[] inputFields = {"Weet u zeker dat u de persoongegevens van de persoon met id: " + id + " wilt verwijderen?",
                                "Dit kan NIET ongedaan gemaakt worden."};

        int option = JOptionPane.showOptionDialog(this, inputFields, "Speler verwijderen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{delete, cancel}, delete);

        if (option == JOptionPane.OK_OPTION) {
            try {
                playerProvider.deletePlayer(id);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Er is iets fout gegaan met het verwijderen van de speler. Probeer het opnieuw.", "Foutmelding", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
