package components;

import backend.MentorProvider;
import components.dialogs.*;
import components.panels.OverviewPanel;
import components.representation.*;
import models.Mentor;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Optional;

public class MentorOverviewPanel extends OverviewPanel {

    private MentorProvider provider;
    private Representor<Mentor> representor;
    private GenericTableModel<Mentor> model;
    private TablePanel tablePanel;

    public MentorOverviewPanel() {
        provider = new MentorProvider();

        representor = new RepresentationBuilder<Mentor>()
                .addColumn("Id"            , Mentor::getId)
                .addColumn("Naam"          , Mentor::getName)
                .addColumn("Telefoonnummer", Mentor::getPhoneNumber)
                .addColumn("Emailadres"    , Mentor::getEmail)
                .build();

        fetchMentorData();
        tablePanel = new TablePanel(model);
        add(tablePanel, BorderLayout.CENTER);
        createButtons();
    }

    private void fetchMentorData() {
        try {
            model = new GenericTableModel<>(provider.getAllMentors(), representor);
        } catch (SQLException exception) {
            exception.printStackTrace();
            model = new GenericTableModel<>(representor);
        }
    }

    @Override
    protected void createButtons() {
        JButton addButton = new JButton("Toevoegen");
        addButton.setPreferredSize(new Dimension(150, 200));
        addButton.addActionListener(e -> new AddMentorDialog(model));

        JButton editButton = new JButton("Wijzigen");
        editButton.setEnabled(false);
        editButton.addActionListener(e -> {

            if(tablePanel.getSelectedRows() == null || tablePanel.getSelectedRows().length < 1) {
                new NoSelectionDialog("Mentor");
            } else {
                int selectedRow = tablePanel.getSelectedRow();
                Optional<Mentor> updatingMentor = findPlayerInList((Integer) model.getValueAt(selectedRow, 0));
                if(!updatingMentor.isPresent()) {
                    // This suggests that we missed an event (delete or edit) and are out of sync with the database
                    // Refresh list with new data, although this should theoretically never happen
                    fetchMentorData();
                    return;
                }

                // Update the mentor
                new AddMentorDialog(model, updatingMentor.get());
            }
        });
        editButton.setPreferredSize(new Dimension(150, 200));

        JButton deleteButton = new JButton("Verwijderen");
        deleteButton.setEnabled(false);
        deleteButton.setPreferredSize(new Dimension(150, 200));
        deleteButton.addActionListener(e -> {
            if(tablePanel.getSelectedRows()  == null || tablePanel.getSelectedRows().length < 1) {
                new NoSelectionDialog("persoon");
            } else {
                int mentorId = (Integer) model.getValueAt(tablePanel.getSelectedRow(), 0);
                Optional<Mentor> possibleMentor = findPlayerInList(mentorId);

                if(possibleMentor.isPresent() && model.indexOf(possibleMentor.get()) != -1) {
                    Mentor mentor = possibleMentor.get();
                    int mentorIndex = model.indexOf(mentor);

                    // Code blocks until the Dialog is closed
                    int result = new DeleteDialog(mentorId).showDialog();

                    if(result == JOptionPane.OK_OPTION) {
                        // Remove data from the model
                        try {
                            provider.deleteMentor(mentorId);
                            model.set(mentorIndex, provider.getMentorById(mentorId));
                            tablePanel.clearSelection();
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Er is iets fout gegaan met het verwijderen van de bekende speler. Probeer het opnieuw.",
                                    "Foutmelding",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } else {
                    // This suggests that we missed an event (delete or edit) and are out of sync with the database
                    // Refresh list with new data, although this should theoretically never happen
                    fetchMentorData();
                    JOptionPane.showMessageDialog(
                            this,
                            "De tabel loopt achter op de database. probeer de bekende speler opnieuw te verwijderen",
                            "Foutmelding",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        tablePanel.addSelectionListener((selectionEvent) -> {
            // Selection is not jet finished. Ignore the event
            if(selectionEvent.getValueIsAdjusting())
                return;

            int[] selectedRows = tablePanel.getSelectedRows();
            if(selectedRows.length == 1) {
                Mentor selectedMentor = model.get(selectedRows[0]);
                if(!selectedMentor.isDeleted()) {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    return;
                }
            }

            // If we cannot find the mentor or the selection count is not 1 disable the buttons
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
        });

        addButtonToPanel(addButton);
        addButtonToPanel(editButton);
        addButtonToPanel(deleteButton);
    }

    private Optional<Mentor> findPlayerInList(int mentorId) {
        return model.stream()
                .filter((mentor) -> mentor.getId() == mentorId)
                .findFirst();
    }
}
