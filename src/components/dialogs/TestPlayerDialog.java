package components.dialogs;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.util.Arrays;

public class TestPlayerDialog extends JDialog {

    private JTextField nameField = new JTextField();

    //adres
    private JTextField streetField = new JTextField();
    private JTextField houseNrField = new JTextField();
    private JTextField toevoegingen = new JTextField();

    private JTextField postcodeField = new JTextField();
    private JTextField cityField = new JTextField();

    //------
    private JComboBox<String> genderBox = new JComboBox<>();

    private JTextField dob = new JTextField();

    private JTextField telephoneNR = new JTextField();
    private JTextField email = new JTextField();

    private JComponent[] fields = {nameField, streetField, houseNrField, postcodeField, cityField, genderBox, dob, telephoneNR, email};


    public TestPlayerDialog() {

        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);
        BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
        EmptyBorder emptyBorder = new EmptyBorder(5, 5, 5, 5);
        contentPane.setBorder(emptyBorder);


        this.getContentPane().setLayout(boxLayout);
        this.setResizable(false);

        this.setSize(new Dimension(500, 800));

        initCombobox();
        addAllFields();
        addButtons();
        this.setVisible(true);

    }

    private void initCombobox() {
        String[] genders = {"Man", "Vrouw", "Overig"};
        Arrays.stream(genders).forEach(g -> genderBox.addItem(g));
        genderBox.setSelectedIndex(2);


    }


    private void addAllFields() {

        String[] fieldnames = {"Naam", "Straat", "Huisnummer", "Postcode", "Woonplaats", "Geslacht",
                "Geboortedatum",
                "Telefoonnummer",
                "Emailadres"};

        int nrOfFields = fields.length;

        for (int i = 0; i < nrOfFields; i++) {

            Label label = new Label(fieldnames[i]);
            label.setFont(new Font("Helvetica", Font.BOLD, 12));

            this.add(label);

            JComponent field = fields[i];
            field.setFont(new Font("Helvetica", Font.PLAIN, 20));
            this.add(field);
            this.add(Box.createRigidArea(new Dimension(300, 9)));
        }
    }

    private void addButtons() {
        JButton submit = new JButton("Toevoegen");
        JButton cancel = new JButton("Annuleer");
        submit.addActionListener(event ->{
            if (!checkAllFields()){
                JOptionPane.showMessageDialog(this, "Iets ging niet helemaal goed");
            }else{
                //TODO
            }
        });

        this.getContentPane().add(cancel);

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

    private boolean checkAllFields() {


        return false;
    }


}
