package components.dialogs.reports;

import backend.MasterclassProvider;
import components.dialogs.InputDialog;
import components.dialogs.exceptions.ExceptionDialog;

import javax.swing.JOptionPane;
import java.sql.SQLException;

public class MasterclassFilterPlayerDialog extends InputDialog {

    MasterclassProvider provider = new MasterclassProvider();

    public MasterclassFilterPlayerDialog() {
        try {
            int rating = Integer.parseInt(JOptionPane.showInputDialog(null, "Op welke minimale rating wilt u filtreren?"));

            try {
                new ReportTableDialog("Spelers filtreren op rating", provider.filterPlayersByRating(rating));
            } catch (SQLException e) {
                new ExceptionDialog("Er is een fout opgetreden bij het ophalen van de gefiltreerde spelers.\nProbeer het opnieuw.");
            }
        } catch (NumberFormatException nfe) {
            // Do nothing. Just close the popup as default.
        }
    }
}
