package components.dialogs.exceptions;

import javax.swing.*;

public class ExceptionDialog extends JDialog {
    public ExceptionDialog(String errorMessage) {
        showError(errorMessage);
    }

    public static void showError(String errorMessage) {
        showError(errorMessage, "Foutmelding!");
    }

    public static void showError(String errorMessage, String title) {
        JOptionPane.showMessageDialog(null, errorMessage, title, JOptionPane.ERROR_MESSAGE);
    }
}
