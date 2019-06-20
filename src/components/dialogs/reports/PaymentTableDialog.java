package components.dialogs.reports;

import components.TablePanel;
import components.dialogs.BasicDialog;
import models.Participant;
import models.Player;
import models.Tournament;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class PaymentTableDialog extends BasicDialog {

    private Tournament toernooi;

    private TablePanel tablePanel;
    private ArrayList<Integer> playerIDs = new ArrayList<>();
    private boolean changedSomething = false;


    public PaymentTableDialog(Tournament tournament) {
        super(true);
        this.toernooi = tournament;

        tablePanel = new TablePanel(createTableModel());
        this.add(tablePanel);
        initButtonPanel();
        this.setVisible(true);

    }

    private void initButtonPanel() {


        JButton safeChanges = new JButton("Accepteer Wijzigingen");
        JButton markAsPaid = new JButton("Markeer als betaald");

        markAsPaid.addActionListener(e -> {


            int id =  (Integer) tablePanel.getModel().getValueAt(tablePanel.getSelectedRow(), 0);
            toernooi.registerPaidParticipation(id);
            playerIDs.add(id);
            tablePanel.setModel(createTableModel());
            changedSomething = true;
        });

        safeChanges.addActionListener(e -> this.dispose());

        JPanel panel = this.createButtonPanel();
        panel.add(safeChanges);
        panel.add(markAsPaid);
        this.add(panel);

    }


    public boolean hasChangedSomething() {
        return changedSomething;
    }

    private DefaultTableModel createTableModel() {
        DefaultTableModel defaultTableModel = new DefaultTableModel();


        Object[] coloumnNames = {"ID", "Naam", "Telefoonnummer", "Postcode"};
        Arrays.stream(coloumnNames).forEach(defaultTableModel::addColumn);


        for (Participant deelname : toernooi.getParticipants()) {
            if (!deelname.isHasPaid()) {
                Player player = deelname.getPlayer();

                Object[] data = new Object[]{player.getId(), player.getName(), player.getTelephoneNR(), player.getAddress().getZipCode()};
                defaultTableModel.addRow(data);
            }
        }

        return defaultTableModel;
    }

    @Override
    public void handleConfirm() {

    }


    public ArrayList<Integer> getPlayerIDs() {
        return playerIDs;
    }
}
