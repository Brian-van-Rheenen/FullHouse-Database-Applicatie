package components.dialogs;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class BasicDialog extends JDialog {

    private JButton submit = new JButton();
    private JButton cancel = new JButton("Annuleer");
    private boolean isForChange;

    public BasicDialog(boolean isForChange) {
        this.isForChange = isForChange;

        // Center in the screen
        this.setLocationRelativeTo(null);

        setLayoutAndBorder();
        this.setResizable(false);

        this.setSize(new Dimension(500, 800));
    }

    public abstract void addAllFields();

    private void setLayoutAndBorder() {
        JPanel contentPane = new JPanel();
        addButtons();
        this.setContentPane(contentPane);
        BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
        EmptyBorder emptyBorder = new EmptyBorder(10, 5, 10, 5);
        contentPane.setBorder(emptyBorder);

        this.getContentPane().setLayout(boxLayout);
    }

    public void unmark(JTextField toUnmark) {
        toUnmark.setForeground(Color.BLACK);
        toUnmark.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    public void mark(JTextField toMark) {
        toMark.setForeground(Color.RED);
        toMark.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
    }

    public void addButtons() {
        if (!this.isForChange) {
            submit.setText("Toevoegen");
        } else {
            cancel.setText("Cancel");
        }

        Font buttonFont = new Font("Helvetica", Font.PLAIN, 20);

        submit.setFont(buttonFont);
        cancel.setFont(buttonFont);

        submit.addActionListener(event -> handleConfirm());

        cancel.addActionListener(e -> this.dispose());

        JPanel buttonPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(1, 2);
        gridLayout.setVgap(10);
        gridLayout.setHgap(10);

        buttonPanel.setLayout(gridLayout);
        buttonPanel.add(submit);
        buttonPanel.add(cancel);

        this.getContentPane().add(buttonPanel);
    }

    /**
     * This method get's called when the user confirms input
     */
    public abstract void handleConfirm();

    public boolean isForChange() {
        return isForChange;
    }
}
