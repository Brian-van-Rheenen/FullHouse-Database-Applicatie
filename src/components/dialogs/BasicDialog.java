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
        this.setModal(true);
        this.setResizable(false);

        this.setSize(new Dimension(500, 800));
    }

    private void setLayoutAndBorder() {
        JPanel contentPane = new JPanel();
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
            submit.setText("Aanpassen");
        }

        Font buttonFont = new Font("Helvetica", Font.PLAIN, 20);

        submit.setFont(buttonFont);
        cancel.setFont(buttonFont);

        submit.addActionListener(event -> handleConfirm());
        cancel.addActionListener(e -> this.dispose());

        JPanel buttonPanel = createButtonPanel();
        buttonPanel.add(submit);
        buttonPanel.add(cancel);

        this.getContentPane().add(buttonPanel);
    }

    public JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(1, 2);
        gridLayout.setVgap(10);
        gridLayout.setHgap(10);

        buttonPanel.setLayout(gridLayout);
        return buttonPanel;
    }

    /**
     * This method get's called when the user confirms input
     */
    public abstract void handleConfirm();

    public boolean validateInput(InputType[] DataTypes, JTextField[] textFields) {
        boolean res = true;

        for (int i = 0; i < DataTypes.length; i++) {
            JTextField textField = textFields[i];
            String input = textField.getText();
            boolean goodInput = DataTypes[i].isGoodInput(input);

            if (goodInput) {
                unmark(textField);

            } else {
                mark(textField);
                res = false;
            }
        }

        return res;
    }

    public void addAllFields(JComponent[] fields, String[] fieldNames) {
        int nrOfFields = fields.length;

        for (int i = 0; i < nrOfFields; i++) {

            JLabel label = new JLabel(fieldNames[i]);
            label.setFont(new Font("Helvetica", Font.BOLD, 12));

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(label);

            this.add(panel);

            JComponent field = fields[i];
            field.setFont(new Font("Helvetica", Font.PLAIN, 20));
            this.add(field);
            this.add(Box.createRigidArea(new Dimension(300, 9)));
        }
    }

    public boolean isForChange() {
        return isForChange;
    }
}
