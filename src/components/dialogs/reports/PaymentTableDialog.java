package components.dialogs.reports;

import components.TablePanel;
import components.dialogs.BasicDialog;
import models.Event;
import models.Participant;
import models.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class PaymentTableDialog extends BasicDialog {

    private TablePanel tablePanel;
    private ArrayList<Participant> notPaidParticipations;
    private ArrayList<Participant> paidParticipations = new ArrayList<>();
    private boolean changedSomething = false;


    public PaymentTableDialog(ArrayList <Participant> notPaidParticipations) {
        super(true);
        this.notPaidParticipations=notPaidParticipations;


        tablePanel = new TablePanel(createTableModel());
        this.add(tablePanel);
        initButtonPanel();
        this.setVisible(true);

    }

    private void initButtonPanel() {


        JButton safeChanges = new JButton("Accepteer Wijzigingen");
        JButton markAsPaid = new JButton("Markeer als betaald");

        markAsPaid.addActionListener(e -> {

            if (!tablePanel.getTable().getSelectionModel().isSelectionEmpty()) {
                int id = (Integer) tablePanel.getModel().getValueAt(tablePanel.getSelectedRow(), 0);

                Optional<Participant> optionalParticipant = notPaidParticipations
                        .stream()
                        .filter(participant -> participant.getPlayer().getId() == id).findAny();

                if (optionalParticipant.isPresent()) {
                    Participant participant = optionalParticipant.get();

                    participant.setHasPaid(true);

                    notPaidParticipations.remove(participant);
                    paidParticipations.add(participant);

                    tablePanel.setModel(createTableModel());
                    changedSomething = true;
                }
            } else {
                JOptionPane.showMessageDialog(this, "U heeft nog geen deelname geselecteerd");
            }
        });
        safeChanges.addActionListener(e -> this.dispose());

        JPanel panel = this.createButtonPanel();
        panel.add(safeChanges);
        panel.add(markAsPaid);
        this.add(panel);

    }

    public ArrayList<Participant> getPaidParticipations() {
        return paidParticipations;
    }

    public boolean hasChangedSomething() {
        return changedSomething;
    }

    private DefaultTableModel createTableModel() {
        DefaultTableModel defaultTableModel = new DefaultTableModel();


        Object[] coloumnNames = {"ID", "Naam", "Telefoonnummer", "Postcode"};
        Arrays.stream(coloumnNames).forEach(defaultTableModel::addColumn);


        for (Participant participant :notPaidParticipations) {
            if (!participant.hasPaid()) {
                Player player = participant.getPlayer();

                Object[] data = new Object[]{player.getId(), player.getName(), player.getTelephoneNR(), player.getAddress().getZipCode()};
                defaultTableModel.addRow(data);
            }
        }

        return defaultTableModel;
    }

    @Override
    public void handleConfirm() {

    }

}
